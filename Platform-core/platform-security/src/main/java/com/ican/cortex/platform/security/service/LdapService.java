package com.ican.cortex.platform.security.service;

import com.ican.cortex.platform.logger.base.APILogger;
import com.ican.cortex.platform.security.security.constants.LDAPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LdapService {
    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private LdapContextSource contextSource;

    @Autowired
    private APILogger logger;



    public void addUser(String uid, String firstName, String lastName, String password) {
        try {
            logger.info(LDAPConstants.TRANSACTION_TYPE, "Attempting to add user with UID: " + uid);

            // Construct the DN for the user using constants
            Name dn = LdapNameBuilder.newInstance()
                    .add(LDAPConstants.UID, uid)
                    .build();

            // Create user attributes
            Map<String, Object> attributes = new HashMap<>();
            attributes.put(LDAPConstants.OBJECT_CLASS, LDAPConstants.OBJECT_CLASSES);
            attributes.put(LDAPConstants.CN, firstName + " " + lastName);
            attributes.put(LDAPConstants.SN, lastName);
            attributes.put(LDAPConstants.GIVEN_NAME, firstName);
            attributes.put(LDAPConstants.UID, uid);
            attributes.put(LDAPConstants.USER_PASSWORD, password);

            // Bind the new entry to the LDAP server
            ldapTemplate.bind(dn + "," + LDAPConstants.BASE_DN, null, LdapAttributesMapper.createAttributes(attributes));

            logger.info(LDAPConstants.TRANSACTION_TYPE, String.format(LDAPConstants.USER_ADD_SUCCESS, uid));
        } catch (Exception e) {
            logger.error(LDAPConstants.TRANSACTION_TYPE, String.format(LDAPConstants.USER_ADD_ERROR, uid), new Throwable(e.getMessage()));
        }
    }

    public Boolean authenticateUser(String username, String password) {
        try {
            logger.info("Authenticating user with username: {}", username);

            contextSource.getContext(LDAPConstants.UID + "=" + username + "," + LDAPConstants.BASE_DN, password);

            logger.info(LDAPConstants.TRANSACTION_TYPE, String.format(LDAPConstants.AUTH_SUCCESS, username));
            return true;
        } catch (Exception e) {
            logger.error("Response", String.format(LDAPConstants.AUTH_FAILED, username), new Throwable(e.getMessage()));
            return false; // Authentication failed
        }
    }

    public List<UserDetails> loadUserByUsername(String uid) {
        logger.info(LDAPConstants.LOAD_USER_DETAILS, uid);
        String filter = "(" + LDAPConstants.UID + "=" + uid + ")";
        try {
            return ldapTemplate.search(
                    LDAPConstants.BASE_DN,  // Base DN from constants
                    filter,  // Search filter
                    (AttributesMapper<UserDetails>) attributes -> {
                        String username = attributes.get(LDAPConstants.UID).get().toString();
                        String password = attributes.get(LDAPConstants.USER_PASSWORD) != null ? attributes.get(LDAPConstants.USER_PASSWORD).get().toString() : "";

                        logger.info(LDAPConstants.LOAD_USER_SUCCESS, username);

                        return new UserDetails() {
                            @Override
                            public Collection<? extends GrantedAuthority> getAuthorities() {
                                return List.of();
                            }

                            @Override
                            public String getPassword() {
                                return password;
                            }

                            @Override
                            public String getUsername() {
                                return username;
                            }
                        };
                    }
            );
        } catch (Exception e) {
            logger.error(LDAPConstants.LOAD_USER_ERROR, uid, new Throwable(e.getMessage()));
            throw e;
        }
    }

    // Helper class to convert the map to Attributes object
    private static class LdapAttributesMapper {
        public static javax.naming.directory.Attributes createAttributes(Map<String, Object> attributes) {
            javax.naming.directory.Attributes ldapAttributes = new javax.naming.directory.BasicAttributes();
            attributes.forEach((key, value) -> {
                javax.naming.directory.Attribute attribute = new javax.naming.directory.BasicAttribute(key);
                if (value instanceof String[]) {
                    for (String val : (String[]) value) {
                        attribute.add(val);
                    }
                } else {
                    attribute.add(value);
                }
                ldapAttributes.put(attribute);
            });
            return ldapAttributes;
        }
    }
}