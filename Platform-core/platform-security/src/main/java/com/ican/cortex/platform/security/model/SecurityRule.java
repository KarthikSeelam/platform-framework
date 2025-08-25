package com.ican.cortex.platform.security.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Naveen Kumar
 */
@Getter
@Setter
public class SecurityRule {
    /**
     * The URL pattern to secure (e.g., /public/**, /user/**, /admin/**)
     */
    private String path;

    /**
     * The access type – supported values: "permitAll" or "hasRole"
     */
    private String access;

    /**
     * The required role if access == "hasRole" (e.g., USER, ADMIN)
     */
    private String role;


}
