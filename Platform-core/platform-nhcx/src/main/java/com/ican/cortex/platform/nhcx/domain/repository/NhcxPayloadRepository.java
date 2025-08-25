package com.ican.cortex.platform.nhcx.domain.repository;

import com.ican.cortex.platform.nhcx.domain.entity.NhcxPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NhcxPayloadRepository extends JpaRepository<NhcxPayload, UUID> {
}
