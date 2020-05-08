package com.sptan.exec.rediscache.service;

import com.sptan.exec.framework.security.JwtUser;
import com.sptan.exec.rediscache.exception.BadRequestException;
import com.sptan.exec.rediscache.service.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;


/**
 * @author lp
 * @date 2018-11-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;


    public JwtUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        UserDTO user = null;
        try {
            user = userService.findByName(username);
        } catch (EntityNotFoundException e) {
            throw new BadRequestException("USER_PASSWORD_ERROR_MSG");
        }
        if (user == null) {
            throw new BadRequestException("USER_PASSWORD_ERROR_MSG");
        } else {
            return createJwtUser(user);
        }
    }

    public UserDetails createJwtUser(UserDTO user) {
        return new JwtUser(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getAvatar(),
            user.getEmail(),
            user.getPhone(),
            null,
            null,
            null,
            user.getEnabled(),
            user.getCreateTime(),
            user.getLastPasswordResetTime()
        );
    }
}
