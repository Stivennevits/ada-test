package com.ada.test.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class I18NConfig {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(new Locale("es", "CO"));
        sessionLocaleResolver.setDefaultTimeZone(TimeZone.getTimeZone(ZoneId.of("America/Bogota")));
        return sessionLocaleResolver;
    }
}