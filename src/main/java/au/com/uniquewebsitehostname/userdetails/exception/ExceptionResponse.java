package au.com.uniquewebsitehostname.userdetails.exception;

import java.util.Date;

public class ExceptionResponse {
    private Date timestamp;
    private String message;

    public ExceptionResponse(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public ExceptionResponse(Date timestamp, String message) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}