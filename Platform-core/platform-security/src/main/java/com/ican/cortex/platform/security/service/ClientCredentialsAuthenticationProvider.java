package com.ican.cortex.platform.security.service;

import java.util.List;

import com.ican.cortex.platform.security.config.SecurityProperties;
import com.ican.cortex.platform.security.model.SecurityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Collections;

/**
 * @author Naveen Kumar
 */
@Component
public class ClientCredentialsAuthenticationProvider implements AuthenticationProvider {

    private final List<SecurityClient> clients;

    @Autowired
    public ClientCredentialsAuthenticationProvider(SecurityProperties securityProperties) {
        this.clients = securityProperties.getClients();
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String clientId = authentication.getName();
        String secret = authentication.getCredentials().toString();

        for (SecurityClient client : clients) {
            if (client.getClientId().equals(clientId) && client.getSecret().equals(secret)) {
                // Successful authentication: assign the ROLE_CLIENT authority.
                return new UsernamePasswordAuthenticationToken(
                        clientId,
                        secret,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")));
            }
        }
        throw new BadCredentialsException("Invalid client credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
