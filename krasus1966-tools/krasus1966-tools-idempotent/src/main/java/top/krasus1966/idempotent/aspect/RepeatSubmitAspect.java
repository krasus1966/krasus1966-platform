package top.krasus1966.idempotent.aspect;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.krasus1966.idempotent.anno.NoRepeatSubmit;
import top.krasus1966.idempotent.properties.IdempotentProperties;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Krasus1966
 * @date 2021/6/15 09:01
 **/
@EnableConfigurationProperties(IdempotentProperties.class)
@Aspect
public class RepeatSubmitAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IdempotentProperties idempotentProperties;

    @Pointcut(value = "@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around(value = "pointCut(noRepeatSubmit)", argNames = "joinPoint,noRepeatSubmit")
    public Object around(ProceedingJoinPoint joinPoint, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        int lockSeconds = noRepeatSubmit.lockTime();
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader(idempotentProperties.getTokenName());
        String path = req.getServletPath();
        String key = token + ":" + path;
        String clientId = UUID.randomUUID().toString();

        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, clientId, lockSeconds, TimeUnit.SECONDS))) {
            Object result = null;
            try {
                result = joinPoint.proceed();
            } finally {
                redisTemplate.delete(key);
            }
            return result;
        } else {
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("code", HttpStatus.LOCKED.value());
            bodyMap.put("msg", "禁止重复提交请求，请稍后再试！");
            ObjectMapper objectMapper = new ObjectMapper();
            return ResponseEntity
                    .status(HttpStatus.LOCKED)
                    .body(objectMapper.writeValueAsString(bodyMap));
        }
    }
}
