package au.com.uniquewebsitehostname.userdetails.interceptor;

import au.com.uniquewebsitehostname.userdetails.exception.IdValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IdValidationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        if(url == null || url.isEmpty()) {
            throw new IdValidationException();
        }

        String userId = getLastPartOfUrl(url);
        if(userId.isEmpty()) {
            throw new IdValidationException();
        }

        try {
            Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            throw new IdValidationException(userId);
        }

        return true;
    }

    private static String getLastPartOfUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
