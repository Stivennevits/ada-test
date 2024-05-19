package com.ada.test.core.service;

import com.ada.test.common.constants.ErrorMessages;
import com.ada.test.common.ex.AdaException;
import com.ada.test.core.components.I18NComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidateSizeService {
    private final I18NComponent i18NComponent;

    public ValidateSizeService(I18NComponent i18NComponent) {
        this.i18NComponent = i18NComponent;
    }

    public void validateSize(String code, String name, String description, String createdBy){
        log.info("CompanyService::validateSize");
        if(code.length() > 10){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.INVALID_SIZE_CODE, 10));
        }
        if(name.length() > 25){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.INVALID_SIZE_NAME, 25));
        }
        if(description.length() > 50){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.INVALID_SIZE_DESCRIPTION, 50));
        }
        if(createdBy.length() > 15){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.INVALID_SIZE_CREATED_BY, 15));
        }
    }
}
