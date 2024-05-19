package com.ada.test.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "COMPANY_VERSION")
public class CompanyVersionRecord {
    @Id
    private Long id;
    private Long companyId;
    private Long versionId;
    private String versionCompanyDescription;
}
