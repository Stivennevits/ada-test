package com.ada.test.app.rest;

import com.ada.test.common.request.CompanyRequest;
import com.ada.test.common.response.CompanyResponse;
import com.ada.test.core.service.CompanyService;
import com.ada.test.domain.model.CompanyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ada.test.common.router.Router.CompanyAPI.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(ROOT)
public class CompanyController {
    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping(GET)
    @ResponseStatus(OK)
    public CompanyRecord getById(@PathVariable Long id){
        log.info("CompanyController::getById --id [{}] ", id);
        return service.getById(id);
    }

    @GetMapping(GET_ENTITIES)
    @ResponseStatus(OK)
    public CompanyResponse getEntities(@RequestParam String code){
        log.info("CompanyController::getEntities --code [{}] ", code);
        return service.getEntities(code);
    }

    @GetMapping(GET_ALL)
    @ResponseStatus(OK)
    public List<CompanyRecord> getAll(){
        log.info("CompanyController::getAll ");
        return service.getAll();
    }



    @PostMapping()
    @ResponseStatus(CREATED)
    public CompanyRecord create(@RequestBody CompanyRequest request){
        log.info("CompanyController::create --code [{}] --name [{}] --description [{}] ", request.getCode(),request.getName() ,request.getDescription());
        return service.create(request);
    }

    @PutMapping(UPDATE)
    @ResponseStatus(OK)
    public CompanyRecord update(@PathVariable Long id ,@RequestBody CompanyRequest request){
        log.info("CompanyController::update --code [{}] --name [{}] --description [{}] ", request.getCode(),request.getName() ,request.getDescription());
        return service.update(id,request);
    }

    @DeleteMapping(DELETE)
    public void delete(@PathVariable Long id){
        log.info("CompanyController::delete --id [{}] ", id);
        service.delete(id);
    }
}
