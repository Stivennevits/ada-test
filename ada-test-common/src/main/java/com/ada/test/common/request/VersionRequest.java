package com.ada.test.common.request;

import lombok.Data;

@Data
public class VersionRequest {
    private Long appId;
    private String version;
    private String description;
}
