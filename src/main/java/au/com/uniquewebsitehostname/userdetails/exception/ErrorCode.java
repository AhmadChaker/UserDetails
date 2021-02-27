package au.com.uniquewebsitehostname.userdetails.exception;

public enum ErrorCode {
    GENERIC(1),
    CIRCUIT_BREAKER_TRIPPED(2),
    USER_AUTH_DETAILS_NOT_FOUND(3),
    USER_DETAILS_NOT_FOUND(4),
    ID_VALIDATION_FAILED(5),
    CONSTRAINT_VALIDATION_FAILED(6),
    ACCESS_DENIED(7);

    private final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
