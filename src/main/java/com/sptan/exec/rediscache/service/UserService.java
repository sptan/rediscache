package com.sptan.exec.rediscache.service;


import com.sptan.exec.rediscache.domain.User;
import com.sptan.exec.rediscache.service.dto.UserDTO;
import com.sptan.exec.rediscache.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The interface User service.
 *
 * @author lp
 * @date 2018 -11-23
 */
public interface UserService {

    /**
     * Find by id user dto.
     *
     * @param id the id
     * @return the user dto
     */
    UserDTO findById(String id);

    /**
     * Create user dto.
     *
     * @param resources the resources
     * @return the user dto
     */
    UserDTO create(User resources);

    /**
     * Update.
     *
     * @param resources the resources
     */
    void update(User resources);

    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(String id);

    /**
     * Find by name user dto.
     *
     * @param userName the user name
     * @return the user dto
     */
    UserDTO findByName(String userName);

    /**
     * Update pass.
     *
     * @param username        the username
     * @param encryptPassword the encrypt password
     */
    void updatePass(String username, String encryptPassword);

    /**
     * Update email.
     *
     * @param username the username
     * @param email    the email
     */
    void updateEmail(String username, String email);

    /**
     * Query all object.
     *
     * @param criteria the criteria
     * @param pageable the pageable
     * @return the object
     */
    Object queryAll(UserQueryCriteria criteria, Pageable pageable);

    /**
     * Query all list.
     *
     * @param criteria the criteria
     * @return the list
     */
    List<UserDTO> queryAll(UserQueryCriteria criteria);

    /**
     * Download.
     *
     * @param queryAll the query all
     * @param response the response
     * @throws IOException the io exception
     */
    void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException;

}
