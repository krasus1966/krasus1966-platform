package top.krasus1966.core.oplog;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentParser;
import cn.hutool.json.JSONUtil;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.RenameCollectionOptions;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.krasus1966.core.base.constant.LoginConstants;
import top.krasus1966.core.oplog.anno.OplogInfo;
import top.krasus1966.core.oplog.entity.OpLogInfo;
import top.krasus1966.core.oplog.util.OplogUtil;
import top.krasus1966.core.web.auth.entity.UserLoginInfo;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.util.login.LoginUtils;
import top.krasus1966.core.web.util.servlet.IPUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/5/3 17:54
 **/
@Service
public class OplogServiceImpl {

    /**
     * 版本号
     */
    @Value("${version}")
    private String version;

    private final MongoTemplate mongoTemplate;
    private final LoginConstants loginConstants;

    public OplogServiceImpl(MongoTemplate mongoTemplate, LoginConstants loginConstants) {
        this.mongoTemplate = mongoTemplate;
        this.loginConstants = loginConstants;

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
        Map<String, String> rtnMap = OplogUtil.converMap(request.getParameterMap());
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
                LoginUtils.getUserLoginInfo(request.getHeader(loginConstants.getHeaderUserToken()));
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
        mongoTemplate.save(oplogs);
//        this.saveOplog(oplogs);
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
}
