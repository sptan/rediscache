package com.sptan.exec.rediscache.domain;

import com.sptan.exec.framework.listener.EntityWithListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lp
 * @date 2018-11-22
 */
@Entity
@Getter
@Setter
@Table(name = "user")
@DynamicUpdate
@EntityListeners(EntityWithListener.class)
public class User implements Serializable {

    @Id
    @GenericGenerator(name = "sequence", strategy = "com.sptan.exec.framework.identifier.SnowflakeIdGenerator")
    @GeneratedValue(generator = "sequence")
    @NotNull(groups = Update.class)
    private String id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @Pattern(regexp = "([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}",
             message = "格式错误")
    private String email;

    private String phone;

    private Boolean enabled;

    private String password;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

    @Column(name = "last_password_reset_time")
    private Date lastPasswordResetTime;

    @Column(name = "create_user_id")
    private String createUserId;

    @Column(name = "create_user_name")
    private String createUserName;

    @Column(name = "update_time")
    @CreationTimestamp
    private Date updateTime;

    @Column(name = "update_user_id")
    private String updateUserId;

    @Column(name = "update_user_name")
    private String updateUserName;

    public @interface Update {
    }
}