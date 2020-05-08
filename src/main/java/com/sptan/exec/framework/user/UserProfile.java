package com.sptan.exec.framework.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liupeng
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    /**
     * 用户ID.
     */
    private String userId;

    /**
     * 用户名称.
     */
    private String userName;
}
