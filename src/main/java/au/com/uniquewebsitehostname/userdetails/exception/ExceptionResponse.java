package au.com.uniquewebsitehostname.userdetails.exception;

import lombok.ToString;

import java.util.Date;

@ToString
public class ExceptionResponse {
    private int code;
    private String message;
    private Date timestamp;

    public ExceptionResponse(String message, ErrorCode code) {
        this.code = code.getErrorCode();
        this.message = message;
        this.timestamp = new Date();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
