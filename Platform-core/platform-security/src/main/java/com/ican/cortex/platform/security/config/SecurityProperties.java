package com.ican.cortex.platform.security.config;

import java.util.List;

import com.ican.cortex.platform.security.model.JwtProperties;
import com.ican.cortex.platform.security.model.SecurityClient;
import com.ican.cortex.platform.security.model.SecurityRule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Naveen Kumar
 */
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

    private List<SecurityRule> rules;
    private List<SecurityClient> clients;
    private JwtProperties jwt = new JwtProperties();



}
