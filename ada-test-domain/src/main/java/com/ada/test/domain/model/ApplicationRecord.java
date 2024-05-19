package com.ada.test.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "APPLICATION")
public class ApplicationRecord {
    @Id
    private Long id;
    private String code;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private String createdBy;
}
