package com.ada.test.app.rest;

import com.ada.test.common.request.ApplicationRequest;
import com.ada.test.core.service.ApplicationService;
import com.ada.test.domain.model.ApplicationRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ada.test.common.router.Router.ApplicationAPI.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@RestController
@RequestMapping(ROOT)
public class ApplicationController {
    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @GetMapping(GET_ALL)
    @ResponseStatus(OK)
    public List<ApplicationRecord> getAll(){
        log.info("ApplicationController::getAll ");
        return service.getAll();
    }

    @GetMapping(GET)
    @ResponseStatus(OK)
    public ApplicationRecord getById(@PathVariable Long id){
        log.info("ApplicationController::getById --id [{}] ", id);
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public ApplicationRecord create(@RequestBody ApplicationRequest request){
        log.info("ApplicationController::create --code [{}] --name [{}] --description [{}] --createdBy [{}] ", request.getCode(),request.getName() ,request.getDescription(), request.getCreatedBy());
        return service.create(request);
    }

    @PutMapping(UPDATE)
    @ResponseStatus(OK)
    public ApplicationRecord update(@PathVariable Long id , @RequestBody ApplicationRequest request){
        log.info("ApplicationController::update --code [{}] --name [{}] --description [{}] --createdBy [{}] ", request.getCode(),request.getName() ,request.getDescription(), request.getCreatedBy());
        return service.update(id,request);
    }

    @DeleteMapping(DELETE)
    public void delete(@PathVariable Long id){
        log.info("ApplicationController::delete --id [{}] ", id);
        service.delete(id);
    }

}
