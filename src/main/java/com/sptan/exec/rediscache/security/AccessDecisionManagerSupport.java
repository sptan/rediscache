package com.sptan.exec.rediscache.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * The type Access decision manager support.
 *
 * @author tony
 * @version 1.0
 */
public class AccessDecisionManagerSupport implements AccessDecisionManager {

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.security.access.AccessDecisionManager#decide(org.springframework.security.core
     * .Authentication, Object, Collection)
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
        throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes.isEmpty()) {
            return;//当前资源没有配置角色,直接通过
        }
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String needRole = (ca).getAttribute();
            //和用户角色进行比对
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("没有权限,拒绝访问!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
