package com.sptan.exec.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2018-11-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppJwtUser implements UserDetails {

    @JsonIgnore
    private String id;

    private String mobile;

    private String username;

    @JsonIgnore
    private String password;

    private String avatar;

    private String email;

    private Collection<GrantedAuthority> authorities;

    private boolean enabled;

    private Date createTime;

    @JsonIgnore
    private Date lastPasswordResetDate;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Collection getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
