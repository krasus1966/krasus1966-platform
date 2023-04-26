package top.krasus1966.core.crypto.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import top.krasus1966.core.crypto.anno.FieldCrypto;
import top.krasus1966.core.crypto.util.AESUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author Krasus1966
 * @date 2022/12/4 21:06
 **/
@Slf4j
//@Aspect
//@Component
public class FieldCryptoHandler {
    @Pointcut("@annotation(top.krasus1966.core.crypto.anno.Crypto)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //加密
        encrypt(joinPoint);

        return joinPoint.proceed();
    }

    public void encrypt(ProceedingJoinPoint joinPoint) {
        Object[] objects = null;
        try {
            objects = joinPoint.getArgs();
            if (objects.length != 0) {
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i] instanceof List) {
                        List<Object> list = (List) objects[i];
                        for (Object o : list) {
                            encryptObject(o);
                        }
                    } else {
                        encryptObject(objects[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密对象
     *
     * @param obj
     * @throws IllegalAccessException
     */
    private void encryptObject(Object obj) throws IllegalAccessException {

        if (Objects.isNull(obj)) {
            log.info("当前需要加密的object为null");
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean containEncryptField = field.isAnnotationPresent(FieldCrypto.class);
            if (containEncryptField) {
                //获取访问权
                field.setAccessible(true);
                String value = AESUtil.encryptToDatabase(String.valueOf(field.get(obj)));
                field.set(obj, value);
            }
        }
    }
}
