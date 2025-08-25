package com.ican.cortex.platform.security.security.constants;

public class LDAPConstants {

    // LDAP DN and object class constants
    public static final String BASE_DN = "ou=users,ou=system";
    public static final String OBJECT_CLASS = "objectClass";
    public static final String[] OBJECT_CLASSES = {"top", "person", "organizationalPerson", "inetOrgPerson"};

    // LDAP attribute constants
    public static final String USER_PASSWORD = "userPassword";
    public static final String UID = "uid";
    public static final String CN = "cn";
    public static final String SN = "sn";
    public static final String GIVEN_NAME = "givenName";

    // Logging messages
    public static final String AUTH_SUCCESS = "Authentication successful for user: {}";
    public static final String AUTH_FAILED = "Authentication failed for user: {}: {}";
    public static final String USER_ADD_SUCCESS = "User with UID: {} successfully added to LDAP";
    public static final String USER_ADD_ERROR = "Error occurred while adding user with UID: {}: {}";
    public static final String LOAD_USER_DETAILS = "Loading user details for UID: {}";
    public static final String LOAD_USER_SUCCESS = "User details loaded successfully for UID: {}";
    public static final String LOAD_USER_ERROR = "Error occurred while loading user details for UID: {}: {}";
    public static final String TRANSACTION_TYPE = "REQUEST";

    // Prevent instantiation of this class
    private LDAPConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated.");
    }
}
