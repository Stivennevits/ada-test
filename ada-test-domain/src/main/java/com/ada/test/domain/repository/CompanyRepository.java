package com.ada.test.domain.repository;

import com.ada.test.domain.dto.ICompanyDto;
import com.ada.test.domain.dto.ICountForeignDto;
import com.ada.test.domain.model.CompanyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
public interface CompanyRepository extends JpaRepository<CompanyRecord, Long> {
    Optional<CompanyRecord> findByCode(String code);
    @Query(name = "CompanyRepository.countCompanyVersion", nativeQuery = true)
    Optional<ICountForeignDto> countCompanyVersion(Long companyId);
    @Query(name = "CompanyRepository.findMaxId", nativeQuery = true)
    Long findMaxId();
    @Query(name = "CompanyRepository.findEntities", nativeQuery = true)
    Optional<ICompanyDto>  findEntities(Long companyId);
}
