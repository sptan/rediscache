package com.sptan.exec.framework.utils;

import com.sptan.exec.framework.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liupeng
 * @version 1.0
 */
@Component
public class WebUtils {
    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public WebUtils() {
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            return ((ServletRequestAttributes) attributes).getRequest();
        } else {
            return null;
        }
    }

    public UserProfile getUser() {
        HttpServletRequest currentRequest = getRequest();
        if (currentRequest == null) {
            return null;
        }
        String tokenValue = currentRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(tokenValue)) {
            return null;
        } else {
            return jwtTokenUtil.getUserProfile(tokenValue);
        }
    }
}
