package com.sptan.exec.rediscache.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author lp
 * @Date 2019年10月24日20:48:53
 */
@Getter
@Setter
public class BaseDTO  implements Serializable {

    private Boolean isDelete;

    private Timestamp createTime;

    private Timestamp updateTime;
}
