package au.com.uniquewebsitehostname.userdetails.configuration;

import au.com.uniquewebsitehostname.userdetails.controller.UserDetailsController;
import au.com.uniquewebsitehostname.userdetails.interceptor.IdValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorsConfiguration implements WebMvcConfigurer {
    @Autowired
    IdValidationInterceptor idValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // For flexibility the pattern only matches userdetails path
        registry.addInterceptor(idValidationInterceptor).addPathPatterns(
                "/**"+ UserDetailsController.UserDetailsPath+"/*");
    }
}
