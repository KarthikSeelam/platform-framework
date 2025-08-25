package com.ican.cortex.platform.vault.service.impl;

import com.ican.cortex.platform.vault.constant.IcanVaultConstant;
import com.ican.cortex.platform.vault.service.IcanVaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IcanVaultServiceImpl implements IcanVaultService {

    @Autowired
    private VaultTemplate vaultTemplate;

    @Override
    public String getSecret(String path) {
        try {
            VaultResponse response = vaultTemplate.read(IcanVaultConstant.secret_data+"/" + path);
            if (response != null && response.getData() != null) {
                Object data = response.getData().get(IcanVaultConstant.data);
                return data != null ? data.toString() : "No secret found at the specified path";
            } else {
                return "No secret found or path is invalid";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving secret: " + e.getMessage();
        }
    }

    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllVersions(String path) {
        try {
            String metadataPath = String.format(IcanVaultConstant.metadata, path);
            VaultResponse metadataResponse = vaultTemplate.read(metadataPath);

            if (metadataResponse == null || metadataResponse.getData() == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            Map<String, Object> versions = (Map<String, Object>) metadataResponse.getData().get("versions");

            if (versions == null || versions.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Map<String, Object>> secretVersions = versions.keySet().stream()
                    .map(version -> {
                        String versionedPath = String.format(IcanVaultConstant.versionedPath, path, version);
                        VaultResponse response = vaultTemplate.read(versionedPath);

                        if (response != null && response.getData() != null) {
                            Map<String, Object> versionData = (Map<String, Object>) response.getData().get(IcanVaultConstant.data);
                            if (versionData != null) {
                                return Map.of(IcanVaultConstant.version, version, IcanVaultConstant.data, versionData);
                            }
                        }
                        return null;
                    })
                    .filter(map -> map != null)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(secretVersions);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getSpecificVersion(String path, int version) {
        try {
            String versionedPath = String.format(IcanVaultConstant.versionedPath, path, version);
            VaultResponse response = vaultTemplate.read(versionedPath);

            if (response != null && response.getData() != null) {
                Map<String, Object> versionData = (Map<String, Object>) response.getData().get(IcanVaultConstant.data);

                if (versionData != null) {
                    return ResponseEntity.ok(versionData);
                } else {
                    return ResponseEntity.ok(Map.of("message", "No data found for the specified version."));
                }
            } else {
                return ResponseEntity.ok(Map.of("message", "No secret found at the specified path or version."));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error retrieving secret: " + e.getMessage()));
        }
    }

    @Override
    public String getFullPath(String path) {
        try {
            VaultResponse response = vaultTemplate.read(path);
            if (response != null && response.getData() != null) {
                Object data = response.getData().get(IcanVaultConstant.data);
                return data != null ? data.toString() : "No secret found at the specified path";
            } else {
                return "No secret found or path is invalid";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving secret: " + e.getMessage();
        }
    }

    @Override
    public String getFullPathWithVersion(String path, Integer version) {
        {
            try {
                // Construct the path for versioned secrets (if version is provided)
                String versionedPath = version != null ? String.format(IcanVaultConstant.versionId, path, version) : path;

                // Fetch the secret from the specified path (with or without version)
                VaultResponse response = vaultTemplate.read(versionedPath);

                // Check if the response is valid and contains data
                if (response != null && response.getData() != null) {
                    Object data = response.getData().get(IcanVaultConstant.data);

                    // Return the secret data if present
                    return data != null ? data.toString() : "No secret found at the specified path";
                } else {
                    return "No secret found or path is invalid";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error retrieving secret: " + e.getMessage();
            }
        }
    }
}
