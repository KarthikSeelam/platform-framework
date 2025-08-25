package com.ican.cortex.platform.nhcx.domain.repository;

import com.ican.cortex.platform.nhcx.domain.entity.NhcxRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NhcxRequestRepository extends JpaRepository<NhcxRequest, UUID> {
    Optional<NhcxRequest> findByApiCallId(String apiCallId);
    Optional<NhcxRequest> findByCorrelationId(String correlationId);
}
