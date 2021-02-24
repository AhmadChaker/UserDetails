package au.com.uniquewebsitehostname.userdetails.utility;

public class UrlPathHelper {
    public static String getLastPartOfUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }
}
