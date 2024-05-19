package com.ada.test.common.mapper;

import com.ada.test.common.request.ApplicationRequest;
import com.ada.test.domain.model.ApplicationRecord;

import java.time.LocalDateTime;

public class ApplicationMapper {
    private ApplicationMapper(){throw new IllegalStateException("ApplicationMapper");}

    public static ApplicationRecord mapToCreate(Long id, ApplicationRequest request){
        ApplicationRecord record = new ApplicationRecord();
        record.setId(id);
        record.setCode(request.getCode());
        record.setName(request.getName());
        record.setDescription(request.getDescription());
        record.setCreatedAt(LocalDateTime.now());
        record.setCreatedBy(request.getCreatedBy());
        return record;
    }

    public static ApplicationRecord mapToUpdate(ApplicationRecord record,ApplicationRequest request){
        record.setCode(request.getCode());
        record.setName(request.getName());
        record.setDescription(request.getDescription());
        record.setCreatedBy(request.getCreatedBy());
        return record;
    }
}
