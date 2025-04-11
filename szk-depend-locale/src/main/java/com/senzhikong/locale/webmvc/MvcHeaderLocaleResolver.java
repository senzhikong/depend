package com.senzhikong.locale.webmvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

/**
 * @author shu.zhou
 */
public class MvcHeaderLocaleResolver implements LocaleResolver {
    @NonNull
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptHeader = request.getHeader(ACCEPT_LANGUAGE);
        if (StringUtils.isBlank(acceptHeader)) {
            return Locale.getDefault();
        }
        return new Locale(acceptHeader);
    }

    @Override
    public void setLocale(@NonNull HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
