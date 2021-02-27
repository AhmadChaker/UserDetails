package au.com.uniquewebsitehostname.userdetails.exception;

public class UserDetailsNotFoundException extends RuntimeException {
    public UserDetailsNotFoundException(String employeeId) {
        super("No user details exist for employeeId: " + employeeId);
    }
}
