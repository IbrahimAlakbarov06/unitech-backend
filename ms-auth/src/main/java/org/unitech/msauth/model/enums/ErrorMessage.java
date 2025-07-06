package org.unitech.msauth.model.enums;

public enum ErrorMessage {
    NOT_FOUND("NOT_FOUND"),
    METHOD_ARGUMENT_NOT_VALID("METHOD_ARGUMENT_NOT_VALID"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    ALREADY_EXISTS("ALREADY_EXISTS");


    private final String displayName;

    ErrorMessage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
