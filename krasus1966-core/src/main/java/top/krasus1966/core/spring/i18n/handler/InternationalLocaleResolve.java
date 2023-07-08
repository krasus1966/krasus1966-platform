package top.krasus1966.core.spring.i18n.handler;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/3 23:13
 **/
public class InternationalLocaleResolve implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String localeLanguage = request.getHeader("Accept-Language");
        if (null == localeLanguage || "".equals(localeLanguage.trim())) {
            localeLanguage = "zh_CN";
        }
        String[] languagePart = localeLanguage.split("_");
        if (languagePart.length != 2) {
            Locale locale = Locale.getDefault();
            LocaleContextHolder.setLocale(locale);
            return locale;
        }
        Locale locale = new Locale(languagePart[0], languagePart[1]);
        LocaleContextHolder.setLocale(locale);
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        if (null == locale) {
            return;
        }
        LocaleContextHolder.setLocale(locale);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        response.setHeader("Accept-Language", language + "_" + country);
    }
}
