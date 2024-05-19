package com.ada.test.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "VERSION")
public class VersionRecord {
    @Id
    private Long id;
    private Long appId;
    private String version;
    private String description;
}
