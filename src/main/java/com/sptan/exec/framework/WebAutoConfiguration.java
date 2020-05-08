package com.sptan.exec.framework;

import com.sptan.exec.framework.resolver.PageableHandlerMethodArgumentResolverExt;
import com.sptan.exec.framework.resolver.UseProfileHandlerMethodArgumentResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author liupeng
 * @version 1.0
 */
@Configuration
@ComponentScan
public class WebAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolverExt());
        argumentResolvers.add(new UseProfileHandlerMethodArgumentResolver());
    }
}
