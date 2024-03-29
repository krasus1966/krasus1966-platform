package top.krasus1966.core.spring.i18n.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import top.krasus1966.core.spring.util.SpringUtil;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/3 23:49
 **/
public class I18NUtils {
    private static final MessageSource messageSource = SpringUtil.getBean(MessageSource.class);

    public static String getMessage(String key, String defaultMessage) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return defaultMessage;
        }
    }

    public static String getMessage(String key, Object[] args,String defaultMessage) {
        try{
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        }catch (Exception e){
            return defaultMessage;
        }
    }
}
