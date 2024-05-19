package com.ada.test.core.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Component
public class I18NComponent {
    private final MessageSource messageSource;
    @Autowired
    public I18NComponent(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Object... args) {
        Locale locale = Locale.getDefault();
        return messageSource.getMessage(key, args, locale);
    }

    public String getMessage(String key, String arg) {
        return messageSource.getMessage(key, new Object[]{arg}, Locale.getDefault());
    }
}