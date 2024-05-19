package com.ada.test.core.service;

import com.ada.test.common.constants.ErrorMessages;
import com.ada.test.common.ex.AdaException;
import com.ada.test.common.mapper.CompanyVersionMapper;
import com.ada.test.common.request.CompanyVersionRequest;
import com.ada.test.common.request.MassiveRequest;
import com.ada.test.common.response.CompanyVersionResponse;
import com.ada.test.core.components.I18NComponent;
import com.ada.test.domain.model.CompanyVersionRecord;
import com.ada.test.domain.repository.CompanyVersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CompanyVersionService {
    private final I18NComponent i18NComponent;
    private final CompanyVersionRepository repository;
    private final ValidateSizeService validateSize;
    private final CompanyService companyService;
    private final VersionService versionService;

    public CompanyVersionService(I18NComponent i18NComponent, CompanyVersionRepository repository, ValidateSizeService validateSize, CompanyService companyService, VersionService versionService) {
        this.i18NComponent = i18NComponent;
        this.repository = repository;
        this.validateSize = validateSize;
        this.companyService = companyService;
        this.versionService = versionService;
    }

    public CompanyVersionRecord getById(Long id) {
        log.info("CompanyVersionService::getById --id [{}] ", id);
        return repository.findById(id).orElseThrow(() -> new AdaException(i18NComponent.getMessage(ErrorMessages.CV_NOT_FOUND_BY_ID, id)));
    }

    public List<CompanyVersionRecord> getAll() {
        log.info("CompanyVersionService::getAll ");
        return repository.findAll();
    }

    public CompanyVersionRecord create(CompanyVersionRequest request) {
        log.info("CompanyVersionService::create --companyId [{}]  --versionId [{}] --description [{}] ", request.getCompanyId(), request.getVersionId(), request.getVersionCompanyDescription());
        companyService.getById(request.getCompanyId());
        versionService.getById(request.getVersionId());
        Optional<CompanyVersionRecord> companyVersionRecord = repository.findByCompanyIdAndVersionId(request.getCompanyId(), request.getVersionId());
        if(companyVersionRecord.isPresent()){
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.CV_ALREADY_EXISTS, request.getCompanyId(), request.getVersionId()));
        }
        Long maxId = repository.findMaxId();
        if(maxId == null){
            maxId = 0L;
            validateSize.validateSize("", "", request.getVersionCompanyDescription(),"");
            return repository.save(CompanyVersionMapper.mapToCreate(maxId,request));

        }else{
            validateSize.validateSize("", "", request.getVersionCompanyDescription(),"");
            return repository.save(CompanyVersionMapper.mapToCreate(maxId,request));
        }
    }

    public CompanyVersionRecord update(Long id, CompanyVersionRequest request) {
        log.info("CompanyVersionService::update --companyId [{}]  --versionId [{}] --description [{}] ", request.getCompanyId(), request.getVersionId(), request.getVersionCompanyDescription());
        CompanyVersionRecord versionRecord = getById(id);
        if((versionRecord.getCompanyId().equals(request.getCompanyId())) && versionRecord.getVersionId().equals(request.getVersionId())){
            validateSize.validateSize("", "", request.getVersionCompanyDescription(),"");
            return repository.save(CompanyVersionMapper.mapToUpdate(versionRecord,request));
        }else{
            companyService.getById(request.getCompanyId());
            versionService.getById(request.getVersionId());
            Optional<CompanyVersionRecord> companyVersionRecord = repository.findByCompanyIdAndVersionId(request.getCompanyId(), request.getVersionId());
            if(companyVersionRecord.isPresent()){
                throw new AdaException(i18NComponent.getMessage(ErrorMessages.CV_ALREADY_EXISTS, request.getCompanyId(), request.getVersionId()));
            }else {
                validateSize.validateSize("", "", request.getVersionCompanyDescription(),"");
                return repository.save(CompanyVersionMapper.mapToUpdate(versionRecord,request));
            }
        }
    }
    public void delete(Long id) {
        log.info("CompanyVersionService::delete --id [{}] ", id);
        CompanyVersionRecord versionRecord = getById(id);
        repository.delete(versionRecord);
    }

    public List<CompanyVersionResponse> massive(MultipartFile file) {
        log.info("CompanyVersionService::massive --file [{}] ", file.getOriginalFilename());
        List<MassiveRequest> requests = new ArrayList<>();
        readAndValidateFile(file,requests);
        log.info("list {}", requests );
        List<CompanyVersionResponse> responses = new ArrayList<>();
        processDuplicateRequests(requests, responses);
        List<Long> companyVersionIds = repository.findIds();
        List<Long> companyIds = repository.findCompanyIds();
        List<Long> versionIds = repository.findVersionIds();
        processRequests(requests, companyVersionIds, companyIds, versionIds, responses);
        processVersionRecords(requests, responses);
        if(!requests.isEmpty()){
            repository.saveAll(CompanyVersionMapper.mapToMassive(requests));
        }
        return responses;
    }

    private void processDuplicateRequests(List<MassiveRequest> requests, List<CompanyVersionResponse> responses) {
        log.info("CompanyVersionService::processDuplicateRequests  ");
        Map<String, MassiveRequest> uniqueRequests = new HashMap<>();
        for (MassiveRequest request : requests) {
            String key = request.getId() + "-" + request.getCompanyId() + "-" + request.getVersionId();
            if (uniqueRequests.containsKey(key)) {
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Registro duplicado");
                responses.add(response);
            } else {
                uniqueRequests.put(key, request);
            }
        }
        requests.clear();
        requests.addAll(uniqueRequests.values());
    }

    private void processVersionRecords(List<MassiveRequest> requests, List<CompanyVersionResponse> responses) {
        log.info("CompanyVersionService::processVersionRecords  ");
        Iterator<MassiveRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            MassiveRequest request = iterator.next();
            Optional<CompanyVersionRecord> versionRecord = repository.findByCompanyIdAndVersionId(request.getCompanyId(), request.getVersionId());
            if (!requests.isEmpty() && versionRecord.isPresent()) {
                iterator.remove();
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Ya existe un registro con el mismo id de empresa y id de versión");
                responses.add(response);
            }
        }
    }


    private void processRequests(List<MassiveRequest> requests, List<Long> companyVersionIds, List<Long> companyIds, List<Long> versionIds, List<CompanyVersionResponse> responses) {
        log.info("CompanyVersionService::processRequests  ");
        List<MassiveRequest> requestsToRemove = new ArrayList<>();
        Iterator<MassiveRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            MassiveRequest request = iterator.next();
            if ((!requests.isEmpty()) && (requests.stream().filter(r -> r.getId().equals(request.getId())).count() > 1)) {
                requestsToRemove.add(request);
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Ya hay un registro dentro del archivo con el mismo ID");
                responses.add(response);
            } else if (!requests.isEmpty() && companyVersionIds.contains(request.getId())) {
                requestsToRemove.add(request);
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Ya existe un registro en COMPANY_VERSION con el mismo ID");
                responses.add(response);
            } else if (!requests.isEmpty() && !companyIds.contains(request.getCompanyId())) {
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "No existe una empresa con el id " + request.getCompanyId());
                responses.add(response);
                requestsToRemove.add(request);
            } else {
                if (!requests.isEmpty() && !versionIds.contains(request.getVersionId())) {
                    CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "No existe una versión con el id " + request.getVersionId());
                    responses.add(response);
                    requestsToRemove.add(request);
                }
            }
        }
        requests.removeAll(requestsToRemove);
    }


    public void readAndValidateFile(MultipartFile file, List<MassiveRequest> requests) {
        log.info("CompanyVersionService::readAndValidateFile  ");
        if (file.isEmpty()) {
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.FILE_IS_EMPTY, file.getOriginalFilename()));
        }
        Pattern pattern = Pattern.compile("^(\\d+)\\|(\\d+)\\|(\\d+)\\|(.+)$");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    throw new AdaException(i18NComponent.getMessage(ErrorMessages.ERROR_INVALID_FILE, line));
                }
                Long id = Long.valueOf(matcher.group(1));
                Long companyId = Long.valueOf(matcher.group(2));
                Long versionId = Long.valueOf(matcher.group(3));
                String description = matcher.group(4);

                MassiveRequest request = new MassiveRequest();
                request.setId(id);
                request.setCompanyId(companyId);
                request.setVersionId(versionId);
                request.setDescription(description);

                requests.add(request);
            }
        } catch (IOException e) {
            throw new AdaException(i18NComponent.getMessage(e.getMessage()));
        }
    }

}
