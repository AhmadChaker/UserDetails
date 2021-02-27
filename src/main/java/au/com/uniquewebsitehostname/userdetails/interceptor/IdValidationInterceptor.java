package au.com.uniquewebsitehostname.userdetails.interceptor;

import au.com.uniquewebsitehostname.userdetails.exception.IdValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.com.uniquewebsitehostname.userdetails.utility.UrlPathHelper;

@Component
public class IdValidationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = UrlPathHelper.getLastPartOfUrl(request.getRequestURI());
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
}
