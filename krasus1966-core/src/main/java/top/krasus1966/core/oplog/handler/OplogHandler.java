package top.krasus1966.core.oplog.handler;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.krasus1966.core.oplog.OplogServiceImpl;
import top.krasus1966.core.oplog.util.OplogUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Krasus1966
 * @date 2022/10/31 16:51
 **/
@Aspect
@Slf4j
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OplogHandler {

    private final OplogServiceImpl oplogService;

    public OplogHandler(OplogServiceImpl oplogService) {
        this.oplogService = oplogService;
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

    @Pointcut("execution(* top.krasus1966.core.web.facade.*BaseController.*(..))")
    public void operLogPointCutBase() {
    }

    @Pointcut("execution(* top.krasus1966.common.file.facade.*Controller.*(..))")
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
            exceptInfo = CharSequenceUtil.sub(OplogUtil.stackTraceToString(e.getClass().getName(),
                    e.getLocalizedMessage(), e.getStackTrace()), 0, 65535);
            throw e;
        } finally {
            stopWatch.stop();
            LocalDateTime endTime = LocalDateTime.now();
            try {
                oplogService.createOpLogInfo(joinPoint,
                        getRequest(), getResp(), beginTime, endTime, result, isSuccess, exceptInfo,
                        stopWatch.getLastTaskTimeMillis());
            } catch (Exception e) {
                log.error("日志记录失败", e);
            }
        }
        return result;
    }
}
