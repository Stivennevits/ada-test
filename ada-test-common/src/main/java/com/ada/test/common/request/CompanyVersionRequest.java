package com.ada.test.common.request;

import lombok.Data;

@Data
public class CompanyVersionRequest {
    private Long companyId;
    private Long versionId;
    private String versionCompanyDescription;
}
