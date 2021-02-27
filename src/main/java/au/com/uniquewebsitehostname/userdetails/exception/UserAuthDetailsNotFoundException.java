package au.com.uniquewebsitehostname.userdetails.exception;

public class UserAuthDetailsNotFoundException extends RuntimeException  {

    public UserAuthDetailsNotFoundException(String username) {
        super("Auth details not found for username: " + username);
    }
}
