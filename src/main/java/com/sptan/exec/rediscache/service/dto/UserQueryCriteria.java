package com.sptan.exec.rediscache.service.dto;

import com.sptan.exec.rediscache.annotation.Query;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author lp
 * @date 2018-11-23
 */
@Data
public class UserQueryCriteria implements Serializable {

    @Query
    private String id;

    @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
    private Set<String> deptIds;

    @Query
    private String username;

    @Query
    private Boolean enabled;

    private Long deptId;

    @Query(type = Query.Type.GREATER_THAN, propName = "createTime")
    private Timestamp startTime;

    @Query(type = Query.Type.LESS_THAN, propName = "createTime")
    private Timestamp endTime;
}
