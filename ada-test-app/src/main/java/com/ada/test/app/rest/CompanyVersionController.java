package com.ada.test.app.rest;

import com.ada.test.common.request.CompanyVersionRequest;
import com.ada.test.common.response.CompanyVersionResponse;
import com.ada.test.core.service.CompanyVersionService;
import com.ada.test.domain.model.CompanyVersionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ada.test.common.router.Router.CompanyVersionAPI.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@RestController
@RequestMapping(ROOT)
public class CompanyVersionController {
    private final CompanyVersionService service;

    public CompanyVersionController(CompanyVersionService service) {
        this.service = service;
    }

    @GetMapping(GET)
    @ResponseStatus(OK)
    public CompanyVersionRecord getById(@PathVariable Long id){
        log.info("CompanyVersionController::getById --id [{}] ", id);
        return service.getById(id);
    }

    @GetMapping(GET_ALL)
    @ResponseStatus(OK)
    public List<CompanyVersionRecord> getAll(){
        log.info("CompanyVersionController::getAll ");
        return service.getAll();
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public CompanyVersionRecord create(@RequestBody CompanyVersionRequest request){
        log.info("CompanyVersionController::create --companyId [{}]  --versionId [{}] --description [{}] ", request.getCompanyId(), request.getVersionId(), request.getVersionCompanyDescription());
        return service.create(request);
    }

    @PutMapping(UPDATE)
    @ResponseStatus(OK)
    public CompanyVersionRecord update(@PathVariable Long id , @RequestBody CompanyVersionRequest request){
        log.info("CompanyVersionController::update --companyId [{}]  --versionId [{}] --description [{}] ", request.getCompanyId(), request.getVersionId(), request.getVersionCompanyDescription());
        return service.update(id,request);
    }

    @DeleteMapping(DELETE)
    public void delete(@PathVariable Long id){
        log.info("CompanyVersionController::delete --id [{}] ", id);
        service.delete(id);
    }

    @PostMapping(MASSIVE)
    @ResponseStatus(OK)
    public List<CompanyVersionResponse> massive (@RequestParam MultipartFile file){
        log.info("CompanyVersionController::massive --file [{}] ", file.getOriginalFilename());
        return service.massive(file);
    }

}
