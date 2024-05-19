package com.ada.test.domain.repository;

import com.ada.test.domain.dto.ICountForeignDto;
import com.ada.test.domain.model.VersionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface VersionRepository extends JpaRepository<VersionRecord, Long> {
    Optional<VersionRecord> findByAppIdAndVersionIgnoreCase(Long appId, String version);
    @Query(name = "VersionRepository.findMaxId", nativeQuery = true)
    Long findMaxId();
    @Query(name = "VersionRepository.countVersion", nativeQuery = true)
    Optional<ICountForeignDto> countVersion(Long versionId);
}
