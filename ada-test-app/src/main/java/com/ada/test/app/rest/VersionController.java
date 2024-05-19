package com.ada.test.app.rest;

import com.ada.test.common.request.VersionRequest;
import com.ada.test.core.service.VersionService;
import com.ada.test.domain.model.VersionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ada.test.common.router.Router.VersionAPI.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(ROOT)
public class VersionController {
    private final VersionService service;

    public VersionController(VersionService service) {
        this.service = service;
    }

    @GetMapping(GET)
    @ResponseStatus(OK)
    public VersionRecord getById(@PathVariable Long id){
        log.info("VersionController::getById --id [{}] ", id);
        return service.getById(id);
    }

    @GetMapping(GET_ALL)
    @ResponseStatus(OK)
    public List<VersionRecord> getAll(){
        log.info("VersionController::getAll ");
        return service.getAll();
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public VersionRecord create(@RequestBody VersionRequest request){
        log.info("VersionController::create --appId [{}] --version [{}] --description [{}] ", request.getAppId(),request.getVersion() ,request.getDescription());
        return service.create(request);
    }

    @PutMapping(UPDATE)
    @ResponseStatus(OK)
    public VersionRecord update(@PathVariable Long id , @RequestBody VersionRequest request){
        log.info("VersionController::update --appId [{}] --version [{}] --description [{}] ", request.getAppId(),request.getVersion() ,request.getDescription());
        return service.update(id,request);
    }

    @DeleteMapping(DELETE)
    public void delete(@PathVariable Long id){
        log.info("VersionController::delete --id [{}] ", id);
        service.delete(id);
    }
}
