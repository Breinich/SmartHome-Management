package com.itcatcetc.smarthome.login;


/**
 * This class is used to store the roles of the users.
 * HOMIE can access all the features of the website.
 * GUEST can only access the public features of the website.
 */
public record Role() {
    public static final String HOMIE = "ROLE_HOMIE";
    public static final String GUEST = "ROLE_GUEST";
}
