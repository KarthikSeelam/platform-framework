package com.ican.cortex.platform.vault.service;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IcanVaultService {

    // Method to fetch a secret from a specified path
    String getSecret(String path);

    // Method to get all versions of a secret from a specified path
    ResponseEntity<List<Map<String, Object>>> getAllVersions(String path);

    // Method to retrieve a specific version of a secret from a specified path
    ResponseEntity<Map<String, Object>> getSpecificVersion(String path, int version);

    // Method to fetch the secret from a specific path (generic version)
    String getFullPath(String path);

    String getFullPathWithVersion(String path, Integer version);
}
