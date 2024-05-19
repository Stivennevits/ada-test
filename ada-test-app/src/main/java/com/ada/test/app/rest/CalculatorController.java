package com.ada.test.app.rest;

import com.ada.test.core.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ada.test.common.router.Router.CalculatorAPI.*;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(ROOT)
public class CalculatorController {
    private final CalculatorService service;

    public CalculatorController(CalculatorService service) {
        this.service = service;
    }

    @GetMapping(ADD)
    @ResponseStatus(OK)
    public double add(@RequestParam Double valueA, @RequestParam Double valueB){
        log.info("ApplicationController::add --[{}] --[{}] ", valueA,valueB);
        return service.add(valueA,valueB);
    }

    @GetMapping(SUBTRACT)
    @ResponseStatus(OK)
    public double subtract(@RequestParam Double valueA, @RequestParam Double valueB){
        log.info("ApplicationController::subtract --[{}] --[{}] ", valueA,valueB);
        return service.subtract(valueA,valueB);
    }

    @GetMapping(MULTIPLY)
    @ResponseStatus(OK)
    public double multiply(@RequestParam Double valueA, @RequestParam Double valueB){
        log.info("ApplicationController::multiply --[{}] --[{}] ", valueA,valueB);
        return service.multiply(valueA,valueB);
    }

    @GetMapping(DIVIDE)
    @ResponseStatus(OK)
    public double divide(@RequestParam Double valueA, @RequestParam Double valueB){
        log.info("ApplicationController::divide --[{}] --[{}] ", valueA,valueB);
        return service.divide(valueA,valueB);
    }

    @GetMapping(POWER)
    @ResponseStatus(OK)
    public double power(@RequestParam Double base, @RequestParam Double exponent) {
        log.info("ApplicationController::power --[{}] --[{}] ", base,exponent);
        return service.power(base, exponent);
    }

    @GetMapping(ROOTS)
    @ResponseStatus(OK)
    public double roots(@RequestParam Double number, @RequestParam Double exponent) {
        log.info("ApplicationController::roots --[{}] --[{}] ", number,exponent);
        return service.roots(number, exponent);
    }


}
