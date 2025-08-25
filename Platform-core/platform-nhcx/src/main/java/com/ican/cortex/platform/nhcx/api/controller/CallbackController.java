package com.ican.cortex.platform.nhcx.api.controller;

import com.ican.cortex.platform.nhcx.service.CallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/nhcx/callback")
@RequiredArgsConstructor
@Slf4j
public class CallbackController {

    private final CallbackService callbackService;

    @PostMapping("/coverage-eligibility/on_check")
    public Mono<ResponseEntity<Void>> onCheck(@RequestBody Map<String, String> body) {
        log.info("Received callback for on_check");
        // Acknowledge immediately and process asynchronously
        return callbackService.handleCallback(body).then(Mono.just(ResponseEntity.ok().build()));
    }

    @PostMapping("/preauth/on_submit")
    public Mono<ResponseEntity<Void>> onPreAuthSubmit(@RequestBody Map<String, String> body) {
        log.info("Received callback for on_submit");
        // Acknowledge immediately and process asynchronously
        return callbackService.handleCallback(body).then(Mono.just(ResponseEntity.ok().build()));
    }
}
