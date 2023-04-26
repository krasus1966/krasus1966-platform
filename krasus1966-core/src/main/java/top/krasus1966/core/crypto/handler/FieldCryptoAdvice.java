package top.krasus1966.core.crypto.handler;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.krasus1966.core.crypto.anno.Crypto;
import top.krasus1966.core.crypto.anno.FieldCrypto;
import top.krasus1966.core.crypto.util.AESUtil;
import top.krasus1966.core.web.entity.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2022/12/4 21:44
 **/
@Slf4j
@RestControllerAdvice(basePackages = {"top.krasus1966"})
public class FieldCryptoAdvice implements ResponseBodyAdvice<R> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.hasMethodAnnotation(Crypto.class) || returnType.hasParameterAnnotation(Crypto.class);
    }

    @Override
    public R beforeBodyWrite(R body, MethodParameter returnType,
                             MediaType selectedContentType, Class selectedConverterType,
                             ServerHttpRequest request, ServerHttpResponse response) {
        if (body.getData() instanceof IPage) {
            IPage bodysPage = (IPage) body.getData();
            for (Object o : bodysPage.getRecords()) {
                Field[] fields = o.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(FieldCrypto.class)) {
                        try {
                            field.setAccessible(true);
                            String value = AESUtil.encryptToDatabase(String.valueOf(field.get(o)));
                            field.set(o, value);
                        } catch (Exception e) {
                            log.error("参数加密返回给前端失败", e);
                        }
                    }
                }
            }
            body.setData(bodysPage);
        } else if (body.getData() instanceof List) {
            List bodys = (List<?>) body.getData();
            for (Object o : bodys) {
                Field[] fields = o.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(FieldCrypto.class)) {
                        try {
                            field.setAccessible(true);
                            String value = AESUtil.encryptToDatabase(String.valueOf(field.get(o)));
                            field.set(o, value);
                        } catch (Exception e) {
                            log.error("参数加密返回给前端失败", e);
                        }
                    }
                }
            }
            body.setData(bodys);
        } else {
            Object data = body.getData();
            Field[] fields = data.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(FieldCrypto.class)) {
                    try {
                        field.setAccessible(true);
                        String value = AESUtil.encryptToDatabase(String.valueOf(field.get(data)));
                        field.set(data, value);
                    } catch (Exception e) {
                        log.error("参数加密返回给前端失败", e);
                    }
                }
            }
            body.setData(data);
        }
        return body;
    }
}
