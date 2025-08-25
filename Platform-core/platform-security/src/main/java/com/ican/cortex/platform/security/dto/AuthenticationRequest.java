package com.ican.cortex.platform.security.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Naveen Kumar
 */
@Setter
@Getter
public class AuthenticationRequest {
    private String userName;
    private String password;

}
