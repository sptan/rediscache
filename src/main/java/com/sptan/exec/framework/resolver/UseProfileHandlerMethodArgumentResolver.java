package com.sptan.exec.framework.resolver;

import com.alibaba.fastjson.JSON;
import com.sptan.exec.framework.user.MemberProfile;
import com.sptan.exec.framework.user.UserProfile;
import com.sptan.exec.framework.utils.FrameworkTokenUtil;
import com.sptan.exec.rediscache.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.SortArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liupeng
 * @version 1.0
 */
public class UseProfileHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(UseProfileHandlerMethodArgumentResolver.class);
    private static final SortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER =
        new SortHandlerMethodArgumentResolver();

    private SortArgumentResolver sortResolver;

    public UseProfileHandlerMethodArgumentResolver() {
        sortResolver = DEFAULT_SORT_RESOLVER;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> targetClass = methodParameter.getParameterType();
        boolean isUserProfile = targetClass.isAssignableFrom(MemberProfile.class);
        boolean isMemberProfile = targetClass.isAssignableFrom(MemberProfile.class);
        boolean result = isUserProfile || isMemberProfile;
        return result;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Class<?> targetClass = methodParameter.getParameterType();
        if (!targetClass.isAssignableFrom(MemberProfile.class)) {
            return WebArgumentResolver.UNRESOLVED;
        }
        Annotation[] annotations = methodParameter.getParameterAnnotations();
        if (annotations.length > 0) {
            Annotation[] var7 = annotations;
            int var8 = annotations.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Annotation annotation = var7[var9];
                logger.info("annotation:" + JSON.toJSONString(annotation));
                if (Optional.class.equals(annotation.annotationType())) {
                    logger.info("annotation is Optional:" + JSON.toJSONString(annotation.annotationType()));
                    return this.buildUserProfile(webRequest, true);
                }
            }
        }

        return this.buildUserProfile(webRequest, false);

    }

    private UserProfile buildUserProfile(NativeWebRequest webRequest, boolean allowNullUserProfile) {
        String tokenValue = webRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(tokenValue)) {
            return null;
        }
        UserProfile userProfile = FrameworkTokenUtil.getUserProfile(tokenValue);
        if (Objects.isNull(userProfile)) {
            if (allowNullUserProfile) {
                return null;
            } else {
                throw new BadRequestException("Bad Authorization");
            }
        } else {
            return userProfile;
        }
    }
}
