package org.unitech.mstransfer.model.enums;

public enum ErrorMessage {
    NOT_FOUND("NOT_FOUND"),
    METHOD_ARGUMENT_NOT_VALID("METHOD_ARGUMENT_NOT_VALID"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS"),
    INVALID_TRANSFER("INVALID_TRANSFER");

    private final String displayName;

    ErrorMessage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
