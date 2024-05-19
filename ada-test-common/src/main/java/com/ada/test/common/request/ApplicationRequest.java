package com.ada.test.common.request;

import lombok.Data;

@Data
public class ApplicationRequest {
    private String code;
    private String name;
    private String description;
    private String createdBy;
}
