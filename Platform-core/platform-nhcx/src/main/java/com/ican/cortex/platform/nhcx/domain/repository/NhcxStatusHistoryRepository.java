package com.ican.cortex.platform.nhcx.domain.repository;

import com.ican.cortex.platform.nhcx.domain.entity.NhcxStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NhcxStatusHistoryRepository extends JpaRepository<NhcxStatusHistory, UUID> {
    List<NhcxStatusHistory> findByCorrelationIdOrderByTimestampDesc(String correlationId);
}
