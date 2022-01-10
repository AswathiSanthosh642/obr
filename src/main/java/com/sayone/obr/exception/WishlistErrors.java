package com.sayone.obr.exception;

public enum WishlistErrors {
    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    INTERNAL_SERVER_ERROR("internal error.Please debug"),
    NO_RECORD_FOUND("no record found.Please debug"),
    NO_BOOK_FOUND("no book found.Please debug"),
    AUTHENTICATION_FAILED("Authentication Failed"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    NO_USER_FOUND("no user found.Please debug"),
    NOT_IN_WISHLIST("no book found in wishlist"),
    RECORD_EXIST("WISH_PRODUCT_EXISTS !!!!!!!"),
    EMAIL_FAIL("EMAIL SENDING FAILED !!!!!!!"),
    EMAIL_TOKEN_EXPIRED("Token has expired!!!"),
    TOKEN_NOT_FOUND("no token found."),
    EMAIL_NOT_VALID("Please enter a valid email id");





    private String errorMessage;

    WishlistErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

