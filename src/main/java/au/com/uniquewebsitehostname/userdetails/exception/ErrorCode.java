package au.com.uniquewebsitehostname.userdetails.exception;

public enum ErrorCode {
    GENERIC(1),
    CIRCUIT_BREAKER_TRIPPED(2),
    USER_AUTH_DETAILS_NOT_FOUND(3),
    USER_DETAILS_NOT_FOUND(4),
    ID_VALIDATION_FAILED(5),
    CONSTRAINT_VALIDATION_FAILED(6),
    ACCESS_FORBIDDEN(7),
    NO_HANDLER_FOUND(8),
    JSON_PROCESSING_ERROR(9),
    HTTP_MESSAGE_UNREADABLE(10),
    CONTROLLER_METHOD_ARG_INVALID(11);

    private final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
