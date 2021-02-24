package au.com.uniquewebsitehostname.userdetails.interceptor;

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
            // TODO throw validation error
        }

        try {
            Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            // TODO log the exception
            // TODO throw validation error
        }

        return true;
    }
}
