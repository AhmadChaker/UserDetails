package au.com.uniquewebsitehostname.userdetails.exception;

public enum ErrorCode {
    GENERIC(0),
    CIRCUIT_BREAKER_TRIPPED(1),
    USER_AUTH_DETAILS_NOT_FOUND(2),
    USER_DETAILS_NOT_FOUND(3),
    ID_VALIDATION_FAILED(4),
    CONSTRAINT_VALIDATION_FAILED(5),
    ACCESS_FORBIDDEN(6),
    NO_HANDLER_FOUND(7),
    HTTP_MESSAGE_UNREADABLE(8),
    CONTROLLER_METHOD_ARG_INVALID(9),
    METHOD_NOT_ALLOWED(10);

    private final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
