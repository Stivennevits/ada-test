package com.ada.test.common.response;

import lombok.Data;

@Data
public class CompanyVersionResponse {
    private Long companyVersionId;
    private Long companyId;
    private Long versionId;
    private String description;
    private String error;
}
