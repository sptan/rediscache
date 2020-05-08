package com.sptan.exec.framework.listener;

import java.util.Date;

/**
 * @author liupeng
 * @version 1.0
 */
public interface NeedCreator {
    /**
     * get 创建者ID.
     *
     * @return creatorId 创建者ID.
     */
    String getCreateUserId();

    /**
     * set 创建者ID.
     *
     * @param createUserId 创建者ID.
     */
    void setCreateUserId(String createUserId);

    /**
     * get 创建者name.
     *
     * @return creatorId 创建者name.
     */
    String getCreateUserName();

    /**
     * set 创建者name.
     *
     * @param createUserName 创建者name.
     */
    void setCreateUserName(String createUserName);

    /**
     * get 创建时间.
     *
     * @return createTime 创建时间.
     */
    Date getCreateTime();

    /**
     * set 创建时间.
     *
     * @param createTime 创建时间.
     */
    void setCreateTime(Date createTime);
}
