package top.krasus1966.core.handler;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentParser;
import cn.hutool.json.JSONUtil;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.RenameCollectionOptions;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.krasus1966.core.anno.oplogs.OplogInfo;
import top.krasus1966.core.constant.PropertiesConstants;
import top.krasus1966.core.entity.auth.UserLoginInfo;
import top.krasus1966.core.entity.oplog.OpLogInfo;
import top.krasus1966.core.entity.web.R;
import top.krasus1966.core.util.login.LoginUtils;
import top.krasus1966.core.util.servlet.IPUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Krasus1966
 * @date 2022/10/31 16:51
 **/
@Aspect
@Slf4j
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class OplogHandler {
    private final PropertiesConstants propertiesConstants;

    private final MongoTemplate mongoTemplate;
    /**
     * 版本号
     */
    @Value("${version}")
    private String version;

    public OplogHandler(PropertiesConstants propertiesConstants, MongoTemplate mongoTemplate) {
        this.propertiesConstants = propertiesConstants;
        this.mongoTemplate = mongoTemplate;
    }

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    private static HttpServletResponse getResp() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.currentRequestAttributes())).getResponse();
    }

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("execution(public * top.krasus1966.*.facade..*(..))")
    public void operLogPointCut() {
    }

    @Pointcut("execution(* top.krasus1966.*.common.core.controller.*BaseController.*(..))")
    public void operLogPointCutBase() {
    }

    @Pointcut("execution(* top.krasus1966.*.facade.*.DownloadFacade.*(..))")
    public void ignore() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     */
    @Around(value = "(operLogPointCut() || operLogPointCutBase()) && !ignore()")
    public Object saveOperLog(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch("log");
        stopWatch.start();
        LocalDateTime beginTime = LocalDateTime.now();
        Object result = null;
        boolean isSuccess = true;
        String exceptInfo = "";
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            isSuccess = false;
            exceptInfo = CharSequenceUtil.sub(stackTraceToString(e.getClass().getName(),
                    e.getLocalizedMessage(), e.getStackTrace()), 0, 65535);
            throw e;
        } finally {
            stopWatch.stop();
            LocalDateTime endTime = LocalDateTime.now();
            try {
                ((OplogHandler) AopContext.currentProxy()).createOpLogInfo(joinPoint,
                        getRequest(), getResp(), beginTime, endTime, result, isSuccess, exceptInfo,
                        stopWatch.getLastTaskTimeMillis());
            } catch (Exception e) {
                log.error("日志记录失败", e);
            }
        }
        return result;
    }

    @Async
    public void createOpLogInfo(ProceedingJoinPoint joinPoint,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                LocalDateTime beginTime,
                                LocalDateTime endTime,
                                Object result,
                                boolean isSuccess,
                                String exceptInfo,
                                long costTime) {
        OpLogInfo oplogs = new OpLogInfo();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        ApiOperation opLog = method.getAnnotation(ApiOperation.class);
        if (opLog != null) {
            String oplogValue = opLog.value();
            // 操作描述
            oplogs.setDescription(oplogValue);
        } else {
            OplogInfo oplogInfo = method.getAnnotation(OplogInfo.class);
            if (oplogInfo != null) {
                String oplogValue = oplogInfo.value();
                // 操作描述
                oplogs.setDescription(oplogValue);
            }
        }
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        // 是否成功
        oplogs.setIsSuccess(isSuccess);
        oplogs.setExceptInfo(exceptInfo);
        // 类名
        oplogs.setClazzName(className);
        // 请求方法
        oplogs.setMethod(methodName);
        // 请求的参数
        Map<String, String> rtnMap = converMap(request.getParameterMap());
        // 将参数所在的数组转换成json
        String params = JSONUtil.toJsonStr(rtnMap);
        // 请求参数
        oplogs.setParams(CharSequenceUtil.sub(params, 0, 65535));
        // 返回结果
        String realResult = JSONUtil.toJsonStr(result);
        oplogs.setResult(CharSequenceUtil.sub(realResult, 0, 65535));
        if (result instanceof R) {
            R r = (R) result;
            oplogs.setResultStatus(r.getCode());
            oplogs.setResultMessage(r.getMsg());
        }
        // 请求URI
        oplogs.setUrl(request.getRequestURI());
        oplogs.setHttpStatus(response.getStatus());
        // 请求类型
        oplogs.setMethodType(request.getMethod());
        // 耗时
        oplogs.setCostTime(costTime);
        // 请求进入时间
        oplogs.setBeginTime(beginTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        // 请求结束时间
        oplogs.setEndTime(endTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        // 操作版本
        oplogs.setVersion(version);
        oplogs.setIpAddress(IPUtils.getRequestIp(request));
        // 浏览器
        String ua = request.getHeader("user-agent");
        if (CharSequenceUtil.isNotBlank(ua)) {
            UserAgent userAgent = UserAgentParser.parse(ua);
            oplogs.setUa(CharSequenceUtil.sub(ua, 0, 65535));
            if (null != userAgent) {
                oplogs.setClientPlatform(userAgent.getPlatform() != null ?
                        userAgent.getPlatform().getName() : null);
                oplogs.setClientOS(userAgent.getOs() != null ? userAgent.getOs().getName() : null);
                oplogs.setClientOSVersion(userAgent.getOsVersion());
                oplogs.setClientBrowser(userAgent.getBrowser() != null ?
                        userAgent.getBrowser().getName() : null);
                oplogs.setClientBrowserVersion(userAgent.getVersion());
                oplogs.setClientIsMobile(userAgent.isMobile());
                oplogs.setClientEngine(userAgent.getEngine() != null ?
                        userAgent.getEngine().getName() : null);
                oplogs.setClientEngineVersion(userAgent.getEngineVersion());
            }
        }

        // 登录状态保存信息
        UserLoginInfo loginInfo =
                LoginUtils.getUserLoginInfo(request.getHeader(propertiesConstants.getHeaderUserToken()));
        if (null != loginInfo) {
            oplogs.setToken(loginInfo.getToken());
            oplogs.setOperatorId(loginInfo.getId());
            oplogs.setOperatorName(loginInfo.getRealName());
        } else {
            oplogs.setToken("");
            oplogs.setOperatorId("");
            oplogs.setOperatorName("noLoginUser");
        }
        oplogs.setCrtTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd " +
                "HH:mm:ss")));
        // 发布记录日志事件
//        publisher.publish(new OpLogSaveEvent(oplogs));
        // 保存日志信息
//        saveOplog(oplogs);
//        mongoTemplate.save(oplogs);
        this.saveOplog(oplogs);
    }

    private void saveOplog(OpLogInfo oplog) {
        MongoDatabase db = mongoTemplate.getDb();
        LocalDate nowDate = LocalDate.now();
        String suffix = nowDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
        MongoCollection dbCollection = db.getCollection("oplog_info");
        dbCollection.renameCollection(new MongoNamespace("oplog_info_" + suffix),
                new RenameCollectionOptions().dropTarget(true));
        dbCollection.insertOne(oplog);
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage,
                                     StackTraceElement[] elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stet : elements) {
            stringBuilder.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n\t" + stringBuilder.toString();
    }
}
