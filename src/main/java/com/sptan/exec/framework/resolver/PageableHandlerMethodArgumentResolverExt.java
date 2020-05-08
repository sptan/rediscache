package com.sptan.exec.framework.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * @author liupeng
 * @version 1.0
 */
public class PageableHandlerMethodArgumentResolverExt extends PageableHandlerMethodArgumentResolver {


    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, @Nullable ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {

        PageRequest pageRequest = (PageRequest) super.resolveArgument(methodParameter, mavContainer, webRequest,
                binderFactory);
        int pageNumber = pageRequest.getPageNumber() <= 1 ? 0 : pageRequest.getPageNumber() - 1;

        Sort sort = pageRequest.getSort();
        if (sort == null || sort.isUnsorted()) {
            sort = Sort.by(Sort.Direction.DESC, "updateTime");
        }

        return PageRequest.of(pageNumber, pageRequest.getPageSize(), sort);
    }
}
