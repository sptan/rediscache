package com.sptan.exec.framework.listener;

import java.util.Date;

/**
 * @author liupeng
 * @version 1.0
 */
public interface NeedUpdater {
    /**
     * get 创建者ID.
     *
     * @return creatorId 创建者ID.
     */
    String getUpdateUserId();

    /**
     * set 创建者ID.
     *
     * @param createUserId 创建者ID.
     */
    void setUpdateUserId(String createUserId);

    /**
     * get 创建者ID.
     *
     * @return creatorId 创建者ID.
     */
    String getUpdateUserName();

    /**
     * set 创建者ID.
     *
     * @param updateUserName 创建者ID.
     */
    void setUpdateUserName(String updateUserName);

    /**
     * get 创建时间.
     *
     * @return createTime 创建时间.
     */
    Date getUpdateTime();

    /**
     * set 创建时间.
     *
     * @param createTime 创建时间.
     */
    void setUpdateTime(Date createTime);
}
