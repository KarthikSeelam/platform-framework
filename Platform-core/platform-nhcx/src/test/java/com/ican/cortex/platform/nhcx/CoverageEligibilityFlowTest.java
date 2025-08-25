package com.ican.cortex.platform.nhcx;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.ican.cortex.platform.nhcx.api.dto.CoverageEligibilityRequestDTO;
import com.ican.cortex.platform.nhcx.api.dto.SubmissionResponseDTO;
import com.ican.cortex.platform.nhcx.domain.repository.NhcxRequestRepository;
import com.ican.cortex.platform.nhcx.messaging.dto.NhcxEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
public class CoverageEligibilityFlowTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private NhcxRequestRepository requestRepository;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().build();

    private static final BlockingQueue<NhcxEvent> kafkaMessages = new LinkedBlockingQueue<>();

    private static String privateKeyPem;
    private static String publicKeyPem;

    static {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();

            publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()) +
                    "\n-----END PUBLIC KEY-----";

            privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                    Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()) +
                    "\n-----END PRIVATE KEY-----";
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate test keys", e);
        }
    }


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("hcx.gateway.base-path", wireMockServer::baseUrl);
        registry.add("hcx.recipient.encryption.public-key-pem", () -> publicKeyPem);
        registry.add("hcx.encryption.private-key-pem", () -> privateKeyPem);
    }

    @KafkaListener(topics = "${kafka.topic.nhcx-audit}", groupId = "test-group-audit")
    public void listenToAudit(NhcxEvent event) {
        kafkaMessages.add(event);
    }

    @BeforeEach
    void setUp() {
        requestRepository.deleteAll();
        kafkaMessages.clear();
        wireMockServer.resetAll();
    }

    @Test
    void testCoverageEligibilityCheck_WhenHcxReturns202_ShouldSucceed() throws InterruptedException {
        // Mock the HCX API endpoint
        wireMockServer.stubFor(post(urlEqualTo("/coverageeligibility/v1/check"))
                .willReturn(aResponse().withStatus(202)));

        // Create a request DTO
        CoverageEligibilityRequestDTO requestDTO = new CoverageEligibilityRequestDTO();
        requestDTO.setPatientId("12345");
        requestDTO.setInsurerId("67890");
        requestDTO.setCoverageId("ABC-123");

        // Call our API endpoint
        SubmissionResponseDTO response = webTestClient.post()
                .uri("/nhcx/coverage-eligibility/check")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubmissionResponseDTO.class)
                .returnResult().getResponseBody();

        // Assertions
        assertNotNull(response);
        assertNotNull(response.getRequestId());
        assertEquals("SUBMITTED", response.getStatus());

        // Verify database persistence
        assertTrue(requestRepository.findById(response.getRequestId()).isPresent());
        assertEquals("SUBMITTED", requestRepository.findById(response.getRequestId()).get().getStatus());

        // Verify Kafka event
        NhcxEvent receivedEvent = kafkaMessages.poll(5, TimeUnit.SECONDS);
        assertNotNull(receivedEvent);
        assertEquals(response.getCorrelationId(), receivedEvent.getCorrelationId());
        assertEquals(NhcxEvent.EventType.COVERAGE_ELIGIBILITY, receivedEvent.getType());
        assertEquals("INITIATED", receivedEvent.getStatus());
    }
}
