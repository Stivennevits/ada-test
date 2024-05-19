package com.ada.test.core.service;

import com.ada.test.common.constants.ErrorMessages;
import com.ada.test.common.ex.AdaException;
import com.ada.test.common.mapper.ApplicationMapper;
import com.ada.test.common.request.ApplicationRequest;
import com.ada.test.core.components.I18NComponent;
import com.ada.test.domain.dto.ICountForeignDto;
import com.ada.test.domain.model.ApplicationRecord;
import com.ada.test.domain.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ApplicationService {
    private final I18NComponent i18NComponent;
    private final ApplicationRepository repository;
    private final ValidateSizeService validateSize;

    public ApplicationService(I18NComponent i18NComponent, ApplicationRepository repository, ValidateSizeService validateSize) {
        this.i18NComponent = i18NComponent;
        this.repository = repository;
        this.validateSize = validateSize;
    }

    public List<ApplicationRecord> getAll() {
        log.info("ApplicationService::getAll ");
        return repository.findAll();
    }

    public ApplicationRecord getById(Long id) {
        log.info("ApplicationService::getById --id [{}] ", id);
        return repository.findById(id).orElseThrow(() -> new AdaException(i18NComponent.getMessage(ErrorMessages.APPLICATION_NOT_FOUND_BY_ID, id)));
    }

    public ApplicationRecord create(ApplicationRequest request) {
        log.info("ApplicationService::create --code [{}] --name [{}] --description [{}] --createdBy [{}] ", request.getCode(),request.getName() ,request.getDescription(), request.getCreatedBy());
        Optional<ApplicationRecord> applicationRecord = repository.findByCode(request.getCode());
        if(applicationRecord.isPresent()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.APPLICATION_ALREADY_EXISTS, request.getCode()));
        }
        Long maxId = repository.findMaxId();
        if(maxId == null){
            maxId = 0L;
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(), request.getCreatedBy());
            return repository.save(ApplicationMapper.mapToCreate(maxId,request));

        }else{
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(), request.getCreatedBy());
            return repository.save(ApplicationMapper.mapToCreate(maxId,request));
        }
    }


    public ApplicationRecord update(Long id, ApplicationRequest request) {
        log.info("ApplicationService::update --code [{}] --name [{}] --description [{}] --createdBy [{}] ", request.getCode(),request.getName() ,request.getDescription(), request.getCreatedBy());
        ApplicationRecord applicationRecord = getById(id);
        if(applicationRecord.getCode().equals(request.getCode())){
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(), request.getCreatedBy());
            return repository.save(ApplicationMapper.mapToUpdate(applicationRecord,request));
        }else{
            Optional<ApplicationRecord> validateApp = repository.findByCode(request.getCode());
            if(validateApp.isPresent()){
                throw new AdaException(i18NComponent.getMessage(ErrorMessages.APPLICATION_ALREADY_EXISTS, request.getCode()));
            }
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(), request.getCreatedBy());
            return repository.save(ApplicationMapper.mapToUpdate(applicationRecord,request));
        }
    }

    public void delete(Long id) {
        log.info("ApplicationService::delete --id [{}] ", id);
        ApplicationRecord applicationRecord = getById(id);
        Optional<ICountForeignDto> iCountCompanyVersionDto = repository.countVersion(id);
        if(iCountCompanyVersionDto.isPresent()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.APPLICATION_IN_USE,iCountCompanyVersionDto.get().getId()));
        }else {
            repository.delete(applicationRecord);
        }
    }
}
