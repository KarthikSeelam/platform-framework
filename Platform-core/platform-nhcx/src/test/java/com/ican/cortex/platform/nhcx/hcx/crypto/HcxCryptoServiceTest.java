package com.ican.cortex.platform.nhcx.hcx.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HcxCryptoServiceTest {

    private HcxCryptoService hcxCryptoService;

    @BeforeEach
    void setUp() throws Exception {
        hcxCryptoService = new HcxCryptoService();

        // Generate RSA key pair for testing
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();

        // Convert keys to PEM format
        String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";

        // PKCS#8 format for private key
        String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()) +
                "\n-----END PRIVATE KEY-----";


        // Use ReflectionTestUtils to set the private fields in HcxCryptoService
        ReflectionTestUtils.setField(hcxCryptoService, "recipientEncryptionPublicKeyPem", publicKeyPem);
        ReflectionTestUtils.setField(hcxCryptoService, "ourEncryptionPrivateKeyPem", privateKeyPem);
    }

    @Test
    void testEncryptDecrypt_RoundTrip_ShouldSucceed() throws Exception {
        // Given
        String originalPayload = "This is a test payload for HCX encryption.";

        // When
        String encryptedPayload = hcxCryptoService.encrypt(originalPayload);
        assertNotNull(encryptedPayload);

        String decryptedPayload = hcxCryptoService.decrypt(encryptedPayload);

        // Then
        assertEquals(originalPayload, decryptedPayload);
    }
}
