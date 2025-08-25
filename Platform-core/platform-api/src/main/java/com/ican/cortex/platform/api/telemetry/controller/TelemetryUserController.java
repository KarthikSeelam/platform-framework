package com.ican.cortex.platform.api.telemetry.controller;

import com.ican.cortex.platform.api.telemetry.impl.TelemetryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ConditionalOnProperty(name = "spring.datasource.enable-telemetry", havingValue = "true")
@RestController
@RequestMapping("/api/telemetry/users")
public class TelemetryUserController {

    @Autowired
    private TelemetryUserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
