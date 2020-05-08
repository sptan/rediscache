package com.sptan.exec.rediscache.security;

import com.sptan.exec.framework.security.JwtUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author lp
 * @date 2018-11-23
 * 返回token
 */
@Getter
@AllArgsConstructor
public class AuthInfo implements Serializable {

    private final String token;

    private final JwtUser user;
}
