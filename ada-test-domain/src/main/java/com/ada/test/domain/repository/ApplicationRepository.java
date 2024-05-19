package com.ada.test.domain.repository;

import com.ada.test.domain.dto.ICountForeignDto;
import com.ada.test.domain.model.ApplicationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationRecord, Long> {
    Optional<ApplicationRecord> findByCode(String code);
    @Query(name = "ApplicationRepository.findMaxId", nativeQuery = true)
    Long findMaxId();
    @Query(name = "ApplicationRepository.countVersion", nativeQuery = true)
    Optional<ICountForeignDto> countVersion(Long appId);
}
