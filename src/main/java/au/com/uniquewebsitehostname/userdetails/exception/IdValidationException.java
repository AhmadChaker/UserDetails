package au.com.uniquewebsitehostname.userdetails.exception;

public class IdValidationException extends RuntimeException {

    public IdValidationException() {
        super("No employee id specified");
    }
    public IdValidationException(String id) {
        super("Employee Id validation failed, id : " + id + " is not an integer");
    }
}
