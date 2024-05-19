package com.ada.test.core.service;

import com.ada.test.common.constants.ErrorMessages;
import com.ada.test.common.ex.AdaException;
import com.ada.test.core.components.I18NComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalculatorService {
    private final I18NComponent i18NComponent;

    public CalculatorService(I18NComponent i18NComponent) {
        this.i18NComponent = i18NComponent;
    }

    public double add(Double valueA, Double valueB) {
        log.info("CalculatorService::add --[{}] --[{}] ", valueA,valueB);
        return valueA + valueB;
    }

    public double subtract(Double valueA, Double valueB) {
        log.info("CalculatorService::subtract --[{}] --[{}] ", valueA,valueB);
        return valueA - valueB;
    }

    public double multiply(Double valueA, Double valueB) {
        log.info("CalculatorService::multiply --[{}] --[{}] ", valueA,valueB);
        return valueA * valueB;
    }

    public double divide(Double valueA, Double valueB) {
        log.info("CalculatorService::divide --[{}] --[{}] ", valueA,valueB);
        if(valueB == 0){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.IMPOSSIBLE_DIVIDE_BY_ZERO));
        }
        return valueA / valueB;
    }

    public double power(Double base, Double exponent) {
        log.info("CalculatorService::power --[{}] --[{}] ", base,exponent);
        return Math.pow(base, exponent);
    }

    public double roots(Double number, Double exponent) {
        log.info("CalculatorService::roots --[{}] --[{}] ", number,exponent);
        if (exponent <= 0) {
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.EXPONENT_NEED_BE_POSITIVE));
        }
        if (number < 0 && exponent % 2 == 0) {
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.IMPOSSIBLE_CALCULATE_NEGATIVE_SQUARE_ROOT));
        }
        return Math.pow(number, 1.0 / exponent);
    }
}
