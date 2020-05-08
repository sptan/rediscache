
package com.sptan.exec.rediscache.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * metadataSource.
 *
 * @author tony
 * @version 1.0
 */
@Component
public class PrivilegeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    public static final String AUTHORIZATION = "Authorization";

    private String[] permits = new String[] {};


    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        final HttpServletRequest currentRequest = ((FilterInvocation) object).getRequest();

        //1.资源是否属于 过滤 资源
        boolean isPermis = Stream.of(permits)
            .map(permiss -> {
                return new AntPathRequestMatcher(permiss);
            })
            .anyMatch(matcher -> matcher.matches(currentRequest));
        if (isPermis) {
            return Collections.emptyList();
        }
        //2.是否传递token
        String tokenValue = currentRequest.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(tokenValue)) {
            new AccessDeniedException("令牌不明确,拒绝访问!");
        }

        //5.没有找到资源, 直接通过
        return Collections.emptyList();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        return Collections.emptyList();
    }

    public void setPermits(String[] permits) {
        this.permits = permits;
    }

}
