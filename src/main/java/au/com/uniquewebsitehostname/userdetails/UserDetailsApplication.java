package au.com.uniquewebsitehostname.userdetails;

import au.com.uniquewebsitehostname.userdetails.controller.UserDetailsController;
import au.com.uniquewebsitehostname.userdetails.interceptor.IdValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UserDetailsApplication implements WebMvcConfigurer {

    @Autowired
    IdValidationInterceptor idValidationInterceptor;

    public static void main(final String[] args) {
        SpringApplication.run(UserDetailsApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // Pattern ensures we only match /userdetails/{id}
        registry.addInterceptor(idValidationInterceptor).addPathPatterns(
                "/**"+UserDetailsController.UserDetailsPath+"/*");
    }

}
