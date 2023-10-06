package top.krasus1966.core.web.handler;

import cn.hutool.core.convert.Convert;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/9/10 23:50
 **/
public class ArgumentHandler extends AbstractMessageConverterMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public ArgumentHandler(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }



    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }



    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> clazz = methodParameter.getParameterType(); // 获取到参数的class对象
        HttpServletRequest servletRequest = ((ServletWebRequest) webRequest).getRequest(); // 获取到HttpServletRequest对象
        Object instance = null;
        if (hasJsonBody(servletRequest)) {
            methodParameter = methodParameter.nestedIfOptional();
            Object arg = readWithMessageConverters(webRequest, methodParameter, methodParameter.getNestedGenericParameterType());
            String name = Conventions.getVariableNameForParameter(methodParameter);

            if (binderFactory != null) {
                WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
                if (arg != null) {
                    validateIfApplicable(binder, methodParameter);
                    if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, methodParameter)) {
                        throw new MethodArgumentNotValidException(methodParameter, binder.getBindingResult());
                    }
                }
                if (mavContainer != null) {
                    mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
                }
            }

            return adaptArgumentIfNecessary(arg, methodParameter);
        } else {
            // 解析x-www-form-urlencoded/form-data
            Iterator<String> parameterNames = webRequest.getParameterNames(); // 取出所有参数名
            instance = clazz.newInstance(); // 创建实体对象
            while (parameterNames.hasNext()) {
                String name = parameterNames.next(); // 请求参数名
                String value = webRequest.getParameter(name); // 请求参数值
                try {
                    Field field = clazz.getDeclaredField(name); // 取出实体对象中与请求参数名一致的字段
                    field.setAccessible(true);
                    // 将请求参数值赋值给实体对象对于的字段,这里用到了ConvertUtils.convert先做了装换，因为form表单中的值均是字符串类型存储的，而实体中的参数可能是int或boolean，因此先要把字符串类型的表单值转换为字段对应的类型再进行赋值
                    field.set(instance, Convert.convert(field.getType(),value));
                } catch (NoSuchFieldException e) {
                    // 实体中没有与参数名称一致的字段
                    logger.warn(String.format("resolver parameter %s failed, no such field in dto ...", name));
                }
            }
        }
        return instance; // 返回值会被赋给参数
    }

    private boolean hasJsonBody(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains("application/json");
    }
}
