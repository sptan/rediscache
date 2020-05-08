package com.sptan.exec.rediscache.service.impl;

import com.sptan.exec.rediscache.domain.User;
import com.sptan.exec.rediscache.exception.BadRequestException;
import com.sptan.exec.rediscache.exception.EntityExistException;
import com.sptan.exec.rediscache.exception.EntityNotFoundException;
import com.sptan.exec.rediscache.repository.UserRepository;
import com.sptan.exec.rediscache.repository.specification.UserSpecification;
import com.sptan.exec.rediscache.service.UserService;
import com.sptan.exec.rediscache.service.dto.UserDTO;
import com.sptan.exec.rediscache.service.dto.UserQueryCriteria;
import com.sptan.exec.rediscache.service.mapper.UserMapper;
import com.sptan.exec.rediscache.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2018-11-23
 */
@Service
@CacheConfig(cacheNames = "user")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_PASSWORD = "123456";

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Object queryAll(UserQueryCriteria criteria, Pageable pageable) {
        Page<User> page =
                userRepository.findAll(UserSpecification.query(criteria), pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UserDTO> queryAll(UserQueryCriteria criteria) {
        List<User> users =
                userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,
                        criteriaBuilder));
        return userMapper.toDto(users);
    }

    @Override
    @Cacheable(key = "#p0")
    public UserDTO findById(String id) {
        User user = userRepository.findById(id).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(), "User", "id", id);
        return userMapper.toDto(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(User resources) {

        if (userRepository.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }

        // 默认密码 123456，此密码是加密后的字符
        String encryptPassword = EncryptUtils.encryptPassword(resources.getPassword());
        resources.setPassword(encryptPassword);
        //默认建立的用户都是可用的
        if (resources.getEnabled() == null) {
            resources.setEnabled(true);
        }
        return userMapper.toDto(userRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(), "User", "id", resources.getId());
        User user1 = userRepository.findByUsername(user.getUsername());

        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }

        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled() == null ? true : resources.getEnabled());
        user.setPhone(resources.getPhone());
        userRepository.save(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'loadUserByUsername:'+#p0")
    public UserDTO findByName(String userName) {
        User user;
        if (ValidationUtil.isEmail(userName)) {
            user = userRepository.findByEmail(userName);
        } else {
            user = userRepository.findByUsername(userName);
        }
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        }
        //用户名需要区分大小写
        if (!userName.equals(user.getUsername())) {
            throw new BadRequestException("用户名或密码错误");
        }
        return userMapper.toDto(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        userRepository.updatePass(username, pass, new Date());
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        userRepository.updateEmail(username, email);
    }

    @Override
    public void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDTO userDTO : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("头像", userDTO.getAvatar());
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("最后修改密码的时间", userDTO.getLastPasswordResetTime());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
