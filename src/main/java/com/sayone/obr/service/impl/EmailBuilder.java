package com.sayone.obr.service.impl;

public class EmailBuilder {

    /**
     * Method is used to get the content of registration email.
     * @param name name of the user.
     * @param link Account activating link.
     * @return String.
     */
    public String buildRegistrationContent(String name,String link) {
        return "Dear "+name+",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href="+link+" target=\"_self\">VERIFY</a></h3>"
                + "Thank you.<br>";
    }

    /**
     * Method is used to get the content of password reset email.
     * @param name name of the user.
     * @param link password reset link.
     * @return String.
     */
    public String buildPasswordResetContent(String name,String link) {
        return "Dear "+name+",<br>"
                + "Please click the link below to change password:<br>"
                + "<h3><a href="+link+" target=\"_self\">VERIFY</a></h3>"
                + "Thank you.<br>";
    }

    /**
     * Method is used to get the content of password reset email.
     * @param name name of the user.
     * @param link password reset link.
     * @return String.
     */
    public String buildUserActivateContent(String name,String link) {
        return "Dear "+name+",<br>"
                + "Your  Email is updated . :<br>"
//                + "<h3><a href="+link+" target=\"_self\">VERIFIED!!!</a></h3>"
                + "Thank you.<br>";
    }
}
