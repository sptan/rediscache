package com.sptan.exec.framework.listener;

import com.sptan.exec.framework.user.MemberProfile;
import com.sptan.exec.framework.user.UserProfile;
import com.sptan.exec.framework.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author liupeng
 * @version 1.0
 */
@Component
public class EntityWithListener {

    @Autowired
    private WebUtils webUtils;

    /**
     * 拦截保存之前的操作.
     *
     * @param persistObj persist object
     */
    @PrePersist
    public void prePersist(Object persistObj) {
        if (persistObj instanceof NeedCreator) {
            NeedCreator nc = (NeedCreator) persistObj;
            setTime(nc::setCreateTime);
            if (nc.getCreateUserId() == null) {
                setUserId(nc::setCreateUserId);
            }
            if (nc.getCreateUserName() == null) {
                setUserName(nc::setCreateUserName);
            }
        }

        //同时初始化更新时间
        this.preUpdate(persistObj);
    }

    /**
     * 拦截更新之前的操作.
     *
     * @param persistObj persist object
     */
    @PreUpdate
    public void preUpdate(Object persistObj) {
        if (persistObj instanceof NeedUpdater) {
            NeedUpdater nm = (NeedUpdater) persistObj;
            setTime(nm::setUpdateTime);
            if (nm.getUpdateUserId() == null) {
                setUserId(nm::setUpdateUserId);
            }
            if (nm.getUpdateUserName() == null) {
                setUserName(nm::setUpdateUserName);
            }
        }
    }

    private void setTime(Consumer<Date> setDate) {
        Date now = new Date();
        setDate.accept(now);
    }

    private void setUserId(Consumer<String> setId) {
        HttpServletRequest currentRequest = WebUtils.getRequest();
        UserProfile userProfile = webUtils.getUser();
        if (Objects.nonNull(currentRequest) && userProfile != null) {
            String userId = userProfile.getUserId();
            if (StringUtils.isEmpty(userId)) {
                return;
            }
            setId.accept(userId);
        }
        if (userProfile instanceof MemberProfile) {
            MemberProfile memberProfile = (MemberProfile) userProfile;
            String userId = memberProfile.getMemberId();
            if (StringUtils.isEmpty(userId)) {
                return;
            }
            setId.accept(userId);
        }
    }

    private void setUserName(Consumer<String> setUserName) {
        HttpServletRequest currentRequest = WebUtils.getRequest();
        UserProfile userProfile = webUtils.getUser();
        if (Objects.nonNull(currentRequest) && userProfile != null) {
            String userName = userProfile.getUserName();
            if (StringUtils.isEmpty(userName)) {
                return;
            }
            setUserName.accept(userName);
        }
        if (userProfile instanceof MemberProfile) {
            MemberProfile memberProfile = (MemberProfile) userProfile;
            String userName = memberProfile.getMobile();
            if (StringUtils.isEmpty(userName)) {
                return;
            }
            setUserName.accept(userName);
        }
    }
}
