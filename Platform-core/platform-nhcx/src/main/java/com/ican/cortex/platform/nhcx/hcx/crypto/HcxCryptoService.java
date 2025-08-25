package com.ican.cortex.platform.nhcx.hcx.crypto;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class HcxCryptoService {

    @Value("${hcx.sender.signing.private-key-pem}")
    private String senderSigningPrivateKeyPem;

    @Value("${hcx.recipient.encryption.public-key-pem}")
    private String recipientEncryptionPublicKeyPem;

    @Value("${hcx.encryption.private-key-pem}")
    private String ourEncryptionPrivateKeyPem;


    public String encrypt(String payload) throws ParseException, JOSEException {
        RSAKey recipientPublicKey = (RSAKey) JWK.parseFromPEMEncodedObjects(recipientEncryptionPublicKeyPem);
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP, EncryptionMethod.A256GCM).build();
        JWEObject jweObject = new JWEObject(header, new Payload(payload));
        jweObject.encrypt(new RSAEncrypter(recipientPublicKey.toRSAPublicKey()));
        return jweObject.serialize();
    }

    public String decrypt(String jweString) throws ParseException, JOSEException {
        JWEObject jweObject = JWEObject.parse(jweString);
        RSAKey ourPrivateKey = (RSAKey) JWK.parseFromPEMEncodedObjects(ourEncryptionPrivateKeyPem);
        jweObject.decrypt(new RSADecrypter(ourPrivateKey.toPrivateKey()));
        return jweObject.getPayload().toString();
    }
}
