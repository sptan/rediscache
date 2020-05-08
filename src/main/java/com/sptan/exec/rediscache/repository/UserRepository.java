package com.sptan.exec.rediscache.repository;

import com.sptan.exec.rediscache.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * The interface User repository.
 *
 * @author lp
 * @date 2018 -11-22
 */
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     */
    User findByUsername(String username);

    /**
     * Find by email user.
     *
     * @param email the email
     * @return the user
     */
    User findByEmail(String email);

    /**
     * Update pass.
     *
     * @param username              the username
     * @param pass                  the pass
     * @param lastPasswordResetTime the last password reset time
     */
    @Modifying
    @Query(value = "update user set password = ?2 , last_password_reset_time = ?3 where username = ?1",
           nativeQuery = true)
    void updatePass(String username, String pass, Date lastPasswordResetTime);

    /**
     * Update email.
     *
     * @param username the username
     * @param email    the email
     */
    @Modifying
    @Query(value = "update user set email = ?2 where username = ?1", nativeQuery = true)
    void updateEmail(String username, String email);
}
