package com.sptan.exec.rediscache.security;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;


/**
 * @author tony
 * @version 1.0
 */
public class PrivilegeSecurityFilter extends AbstractSecurityInterceptor implements Filter {


    private SecurityMetadataSource securityMetadataSource;

    private PrivilegeSecurityFilter() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        FilterInvocation filterInvocation = new FilterInvocation(request, response, chain);
        InterceptorStatusToken token = super.beforeInvocation(filterInvocation);
        try {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return securityMetadataSource;
    }

    /**
     * @author tony
     * @version 1.0
     */
    public static class PrivilegeSecurityFilterBuild {

        private PrivilegeSecurityFilter securityFilterSupport;

        public PrivilegeSecurityFilterBuild() {
            securityFilterSupport = new PrivilegeSecurityFilter();
        }

        public PrivilegeSecurityFilterBuild setAuthenticationManager(AuthenticationManager authenticationManager) {
            securityFilterSupport.setAuthenticationManager(authenticationManager);
            return this;
        }

        public PrivilegeSecurityFilterBuild setSecurityMetadataSource(SecurityMetadataSource securityMetadataSource) {
            securityFilterSupport.securityMetadataSource = securityMetadataSource;
            return this;
        }

        public PrivilegeSecurityFilter build() {
            securityFilterSupport.setAccessDecisionManager(new AccessDecisionManagerSupport());
            return securityFilterSupport;
        }
    }

}
