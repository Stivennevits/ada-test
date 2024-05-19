package com.ada.test.domain.repository;

import com.ada.test.domain.model.CompanyVersionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyVersionRepository extends JpaRepository<CompanyVersionRecord, Long> {
    Optional<CompanyVersionRecord> findByCompanyIdAndVersionId(Long companyId, Long versionId);
    @Query(name = "CompanyVersionRepository.countVersion", nativeQuery = true)
    Long findMaxId();
    @Query(name = "CompanyVersionRepository.findIds", nativeQuery = true)
    List<Long> findIds();
    @Query(name = "CompanyVersionRepository.findCompanyId", nativeQuery = true)
    List<Long> findCompanyIds();
    @Query(name = "CompanyVersionRepository.findVersionIds", nativeQuery = true)
    List<Long> findVersionIds();
}
