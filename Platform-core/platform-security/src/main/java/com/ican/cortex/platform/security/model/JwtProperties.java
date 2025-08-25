package com.ican.cortex.platform.security.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Naveen Kumar
 */
@Getter
@Setter
public class JwtProperties {

    /**
     * The secret key used for signing and verifying JWT tokens.
     */
    private String secret;
    /**
     * The validity period of the token in milliseconds.
     */
    private long validity;
}
