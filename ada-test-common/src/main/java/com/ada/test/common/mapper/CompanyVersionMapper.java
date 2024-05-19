package com.ada.test.common.mapper;


import com.ada.test.common.request.CompanyVersionRequest;
import com.ada.test.common.request.MassiveRequest;
import com.ada.test.common.response.CompanyVersionResponse;
import com.ada.test.domain.model.CompanyVersionRecord;

import java.util.ArrayList;
import java.util.List;

public class CompanyVersionMapper {
    private CompanyVersionMapper(){throw new IllegalStateException("CompanyVersionMapper");}

    public static CompanyVersionRecord mapToCreate(Long id, CompanyVersionRequest request){
        CompanyVersionRecord companyVersionRecord = new CompanyVersionRecord();
        companyVersionRecord.setId(id);
        companyVersionRecord.setCompanyId(request.getCompanyId());
        companyVersionRecord.setVersionId(request.getVersionId());
        companyVersionRecord.setVersionCompanyDescription(request.getVersionCompanyDescription());
        return companyVersionRecord;
    }

    public static List<CompanyVersionRecord> mapToMassive(List<MassiveRequest> requests) {
        List<CompanyVersionRecord> records = new ArrayList<>();
        for (MassiveRequest request : requests) {
            CompanyVersionRecord companyVersionRecord = new CompanyVersionRecord();
            companyVersionRecord.setId(request.getId());
            companyVersionRecord.setCompanyId(request.getCompanyId());
            companyVersionRecord.setVersionId(request.getVersionId());
            companyVersionRecord.setVersionCompanyDescription(request.getDescription());
            records.add(companyVersionRecord);
        }
        return records;
    }

    public static CompanyVersionResponse mapToResponse(MassiveRequest massiveRequest, String error) {
        CompanyVersionResponse response = new CompanyVersionResponse();
        response.setCompanyVersionId(massiveRequest.getId());
        response.setCompanyId(massiveRequest.getCompanyId());
        response.setVersionId(massiveRequest.getVersionId());
        response.setDescription(massiveRequest.getDescription());
        response.setError(error);
        return response;
    }

    public static CompanyVersionRecord mapToUpdate(CompanyVersionRecord companyVersionRecord, CompanyVersionRequest request){
        companyVersionRecord.setCompanyId(request.getCompanyId());
        companyVersionRecord.setVersionId(request.getVersionId());
        companyVersionRecord.setVersionCompanyDescription(request.getVersionCompanyDescription());
        return companyVersionRecord;
    }
}
