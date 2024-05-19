package com.ada.test.core.service;

import com.ada.test.common.constants.ErrorMessages;
import com.ada.test.common.ex.AdaException;
import com.ada.test.common.mapper.VersionMapper;
import com.ada.test.common.request.VersionRequest;
import com.ada.test.core.components.I18NComponent;
import com.ada.test.domain.dto.ICountForeignDto;
import com.ada.test.domain.model.VersionRecord;
import com.ada.test.domain.repository.VersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VersionService {
    private final I18NComponent i18NComponent;
    private final VersionRepository repository;
    private final ValidateSizeService validateSize;
    private final ApplicationService applicationService;

    public VersionService(I18NComponent i18NComponent, VersionRepository repository, ValidateSizeService validateSize, ApplicationService applicationService) {
        this.i18NComponent = i18NComponent;
        this.repository = repository;
        this.validateSize = validateSize;
        this.applicationService = applicationService;
    }

    public VersionRecord getById(Long id) {
        log.info("VersionService::getById --id [{}] ", id);
        return repository.findById(id).orElseThrow(() -> new AdaException(i18NComponent.getMessage(ErrorMessages.VERSION_NOT_FOUND_BY_ID, id)));
    }

    public List<VersionRecord> getAll() {
        log.info("VersionService::getAll ");
        return repository.findAll();
    }

    public VersionRecord create(VersionRequest request) {
        log.info("VersionService::create --appId [{}] --version [{}] --description [{}] ", request.getAppId(),request.getVersion() ,request.getDescription());
        applicationService.getById(request.getAppId());
        Optional<VersionRecord> versionRecord = repository.findByAppIdAndVersionIgnoreCase(request.getAppId(), request.getVersion());
        if(versionRecord.isPresent()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.VERSION_ALREADY_EXISTS, request.getAppId(), request.getVersion()));
        }
        Long maxId = repository.findMaxId();
        if(maxId == null){
            maxId = 0L;
            validateSize.validateSize("", "", request.getDescription(),request.getVersion());
            return repository.save(VersionMapper.mapToCreate(maxId,request));

        }else{
            validateSize.validateSize("", "", request.getDescription(),request.getVersion());
            return repository.save(VersionMapper.mapToCreate(maxId,request));
        }
    }

    public VersionRecord update(Long id, VersionRequest request) {
        log.info("VersionController::update --appId [{}] --version [{}] --description [{}] ", request.getAppId(),request.getVersion() ,request.getDescription());
        VersionRecord versionRecord = getById(id);
        if((versionRecord.getAppId() == request.getAppId() && versionRecord.getVersion().equals(request.getVersion()))){
            return repository.save(VersionMapper.mapToUpdate(versionRecord, request));
        }else{
            applicationService.getById(request.getAppId());
            Optional<VersionRecord> validateVersion = repository.findByAppIdAndVersionIgnoreCase(request.getAppId(), request.getVersion());
            if(validateVersion.isPresent()){
                throw new AdaException(i18NComponent.getMessage(ErrorMessages.VERSION_ALREADY_EXISTS, request.getAppId(), request.getVersion()));
            }
            validateSize.validateSize("", "", request.getDescription(),request.getVersion());
            return repository.save(VersionMapper.mapToUpdate(versionRecord,request));
        }

    }

    public void delete(Long id) {
        log.info("VersionController::delete --id [{}] ", id);
        VersionRecord versionRecord = getById(id);
        Optional<ICountForeignDto> iCountCompanyVersionDto = repository.countVersion(id);
        if(iCountCompanyVersionDto.isPresent()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.VERSION_IN_USE,iCountCompanyVersionDto.get().getId()));
        }else {
            repository.delete(versionRecord);
        }
    }
}
