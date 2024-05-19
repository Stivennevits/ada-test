package com.ada.test.core.service;


import com.ada.test.common.constants.ErrorMessages;
import com.ada.test.common.ex.AdaException;
import com.ada.test.common.mapper.CompanyMapper;
import com.ada.test.common.request.CompanyRequest;
import com.ada.test.common.response.CompanyResponse;
import com.ada.test.core.components.I18NComponent;
import com.ada.test.domain.dto.ICompanyDto;
import com.ada.test.domain.dto.ICountForeignDto;
import com.ada.test.domain.model.CompanyRecord;
import com.ada.test.domain.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CompanyService {
    private final I18NComponent i18NComponent;
    private final CompanyRepository repository;
    private final ValidateSizeService validateSize;

    public CompanyService(I18NComponent i18NComponent, CompanyRepository repository, ValidateSizeService validateSize) {
        this.i18NComponent = i18NComponent;
        this.repository = repository;
        this.validateSize = validateSize;
    }

    public CompanyRecord getById(Long id) {
        log.info("CompanyService::getById --id [{}] ", id);
        return repository.findById(id).orElseThrow(() -> new AdaException(i18NComponent.getMessage(ErrorMessages.COMPANY_NOT_FOUND_BY_ID, id)));
    }

    public CompanyResponse getEntities(String code) {
        log.info("CompanyService::getEntities --code [{}] ", code);
        Optional<CompanyRecord> companyRecord = repository.findByCode(code);
        if(companyRecord.isEmpty()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.COMPANY_NOT_FOUND_BY_CODE, code));
        }
        Optional<ICompanyDto> iCompanyDto = repository.findEntities(companyRecord.get().getId());
        if(iCompanyDto.isEmpty()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.COMPANY_NOT_HAVE_ENTITIES, code));
        }else {
            return CompanyMapper.mapToResponse(iCompanyDto);
        }
    }
    public List<CompanyRecord> getAll() {
        log.info("CompanyService::getAll ");
        return repository.findAll();
    }

    public CompanyRecord create(CompanyRequest request) {
        log.info("CompanyService::create --code [{}] --name [{}] --description [{}] ", request.getCode(),request.getName() ,request.getDescription());
        Optional<CompanyRecord> companyRecord = repository.findByCode(request.getCode());
        if(companyRecord.isPresent()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.COMPANY_ALREADY_EXISTS, request.getCode()));
        }
        Long maxId = repository.findMaxId();
        if(maxId == null){
            maxId = 0L;
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(),"");
            return repository.save(CompanyMapper.mapToCreate(maxId,request));

        }else{
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(),"");
            return repository.save(CompanyMapper.mapToCreate(maxId,request));
        }
    }


    public CompanyRecord update(Long id,CompanyRequest request) {
        log.info("CompanyService::update --code [{}] --name [{}] --description [{}] ", request.getCode(),request.getName() ,request.getDescription());
        CompanyRecord companyRecord = getById(id);
        if(companyRecord.getCode().equals(request.getCode())){
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(),"");
            return repository.save(CompanyMapper.mapToUpdate(companyRecord,request));
        }else{
            Optional<CompanyRecord> validateCompany = repository.findByCode(request.getCode());
            if(validateCompany.isPresent()){
                throw new AdaException(i18NComponent.getMessage(ErrorMessages.COMPANY_ALREADY_EXISTS, request.getCode()));
            }
            validateSize.validateSize(request.getCode(), request.getName(), request.getDescription(),"");
            return repository.save(CompanyMapper.mapToUpdate(companyRecord,request));
        }
    }

    public void delete(Long id) {
        log.info("CompanyService::delete --id [{}] ", id);
        CompanyRecord companyRecord = getById(id);
        Optional<ICountForeignDto> iCountCompanyVersionDto = repository.countCompanyVersion(id);
        if(iCountCompanyVersionDto.isPresent()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.COMPANY_IN_USE,iCountCompanyVersionDto.get().getId()));
        }else {
            repository.delete(companyRecord);
        }
    }


}
