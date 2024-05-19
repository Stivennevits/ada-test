package com.ada.test.common.mapper;


import com.ada.test.common.request.CompanyRequest;
import com.ada.test.common.request.VersionRequest;
import com.ada.test.domain.model.CompanyRecord;
import com.ada.test.domain.model.VersionRecord;

public class VersionMapper {
    private VersionMapper(){throw new IllegalStateException("VersionMapper");}

    public static VersionRecord mapToCreate(Long id, VersionRequest request){
        VersionRecord record = new VersionRecord();
        record.setId(id);
        record.setAppId(request.getAppId());
        record.setVersion(request.getVersion());
        record.setDescription(request.getDescription());
        return record;
    }

    public static VersionRecord mapToUpdate(VersionRecord versionRecord, VersionRequest request){
        versionRecord.setAppId(request.getAppId());
        versionRecord.setVersion(request.getVersion());
        versionRecord.setDescription(request.getDescription());
        return versionRecord;
    }
}
