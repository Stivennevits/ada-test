package com.ada.test.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "COMPANY")
public class CompanyRecord {
    @Id
    private Long id;
    private String code;
    private String name;
    private String description;
}
