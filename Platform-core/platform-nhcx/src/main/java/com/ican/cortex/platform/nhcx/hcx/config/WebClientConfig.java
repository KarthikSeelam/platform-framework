package com.ican.cortex.platform.nhcx.hcx.config;

import com.ican.cortex.platform.nhcx.hcx.service.HcxHeaderGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Configuration
public class WebClientConfig {

    public static final String CORRELATION_ID_ATTR = "X-Correlation-ID";
    public static final String API_CALL_ID_ATTR = "X-Api-Call-ID";
    public static final String WORKFLOW_ID_ATTR = "X-Workflow-ID";


    @Value("${hcx.gateway.base-path}")
    private String hcxGatewayBasePath;

    @Bean
    public WebClient hcxWebClient(HcxHeaderGenerator hcxHeaderGenerator) {
        return WebClient.builder()
                .baseUrl(hcxGatewayBasePath)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(addHcxHeaders(hcxHeaderGenerator))
                .build();
    }

    private ExchangeFilterFunction addHcxHeaders(HcxHeaderGenerator hcxHeaderGenerator) {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            String correlationId = (String) clientRequest.attribute(CORRELATION_ID_ATTR)
                    .orElse(UUID.randomUUID().toString());
            String apiCallId = (String) clientRequest.attribute(API_CALL_ID_ATTR)
                    .orElse(UUID.randomUUID().toString());
            String workflowId = (String) clientRequest.attribute(WORKFLOW_ID_ATTR)
                    .orElse(null);


            Map<String, String> hcxHeaders = hcxHeaderGenerator.generateHeaders(correlationId, apiCallId, workflowId);

            return Mono.just(ClientRequest.from(clientRequest)
                    .headers(headers -> hcxHeaders.forEach(headers::add))
                    .build());
        });
    }
}
