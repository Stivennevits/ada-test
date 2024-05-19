package com.ada.test.common.mapper;

import com.ada.test.common.request.CompanyRequest;
import com.ada.test.common.response.CompanyResponse;
import com.ada.test.domain.dto.ICompanyDto;
import com.ada.test.domain.model.CompanyRecord;

import java.util.Optional;

public class CompanyMapper {
    private CompanyMapper(){throw new IllegalStateException("CompanyMapper");}

    public static CompanyRecord mapToCreate(Long id,CompanyRequest request){
        CompanyRecord companyRecord = new CompanyRecord();
        companyRecord.setId(id);
        companyRecord.setCode(request.getCode());
        companyRecord.setName(request.getName());
        companyRecord.setDescription(request.getDescription());
        return companyRecord;
    }

    public static CompanyRecord mapToUpdate(CompanyRecord companyRecord, CompanyRequest request){
        companyRecord.setCode(request.getCode());
        companyRecord.setName(request.getName());
        companyRecord.setDescription(request.getDescription());
        return companyRecord;
    }

    public static CompanyResponse mapToResponse(Optional<ICompanyDto> dto){
        CompanyResponse response = new CompanyResponse();
        response.setCode(dto.get().getCode());
        response.setName(dto.get().getName());
        response.setAppName(dto.get().getApp());
        response.setVersion(dto.get().getVersion());
        return response;
    }
}