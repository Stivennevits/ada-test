package com.ada.test.common.request;

import lombok.Data;

@Data
public class MassiveRequest {
    private Long id;
    private Long companyId;
    private Long versionId;
    private String description;
}
