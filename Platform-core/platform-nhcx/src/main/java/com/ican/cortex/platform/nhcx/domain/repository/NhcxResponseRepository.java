package com.ican.cortex.platform.nhcx.domain.repository;

import com.ican.cortex.platform.nhcx.domain.entity.NhcxResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NhcxResponseRepository extends JpaRepository<NhcxResponse, UUID> {
}
