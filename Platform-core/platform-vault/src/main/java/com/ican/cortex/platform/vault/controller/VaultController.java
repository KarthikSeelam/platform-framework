package com.ican.cortex.platform.vault.controller;


import com.ican.cortex.platform.vault.service.IcanVaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class VaultController {

    @Autowired
    private IcanVaultService vaultService;

    @GetMapping("/vault-secret")
    public String getVaultSecret(@RequestHeader String path) {
        try {
            return vaultService.getSecret(path);
        } catch (IllegalArgumentException ex) {
            // Handle invalid arguments
            return "Invalid request: " + ex.getMessage();
        } catch (Exception ex) {
            // Handle generic exceptions
            return "An error occurred while retrieving the secret: " + ex.getMessage();
        }
    }

    @GetMapping("/vault-versions")
    public ResponseEntity<List<Map<String, Object>>> getAllVersions(@RequestHeader String path) {
        return vaultService.getAllVersions(path);
    }

    @GetMapping("/vault-secret-specific-version")
    public ResponseEntity<Map<String, Object>> getSpecificVersion(
            @RequestHeader String path,
            @RequestHeader int version) {
        return vaultService.getSpecificVersion(path, version);
    }

    /*
    /**
     * Endpoint to retrieve the secret stored at a specified path from Vault.
     * The path is provided via the request header.
     *
     * @param path the path of the secret to retrieve (e.g., `helpme/data/my-help`).
     *
     * @return a String representing the secret data at the specified path, or an error message.
     *
     * @throws IllegalArgumentException if the provided path is invalid.
     * @throws Exception if an error occurs while retrieving the secret from Vault.
      if URL like this
        http://127.0.0.1:8200/ui/vault/secrets/helpme/kv/list

        Enable a Secrets Engine
            Path -- helpme
                Create Secret --
                    Path for this secret -- my-help
                        Secret data - key and value
    helpme/data/my-help
    */
    @GetMapping("/vault-fullpath")
    public String getFullpath(
            @RequestHeader String path) {
        try {
            return vaultService.getFullPath(path);
        } catch (IllegalArgumentException ex) {
            // Handle invalid arguments
            return "Invalid request: " + ex.getMessage();
        } catch (Exception ex) {
            // Handle generic exceptions
            return "An error occurred while retrieving the secret: " + ex.getMessage();
        }
    }

    @GetMapping("/vault-fullpath-version")
    public String getFullpathWithVersion(
            @RequestHeader String path, @RequestHeader Integer version) {
        try {
            return vaultService.getFullPathWithVersion(path, version);
        } catch (IllegalArgumentException ex) {
            // Handle invalid arguments
            return "Invalid request: " + ex.getMessage();
        } catch (Exception ex) {
            // Handle generic exceptions
            return "An error occurred while retrieving the secret: " + ex.getMessage();
        }
    }
}
