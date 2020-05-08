package com.sptan.exec.rediscache.service.mapper;


import com.sptan.exec.rediscache.base.BaseMapper;
import com.sptan.exec.rediscache.domain.User;
import com.sptan.exec.rediscache.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * The interface User mapper.
 *
 * @author lp
 * @date 2018 -11-23
 */
@Mapper(componentModel = "spring", uses = {
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDTO, User> {

    /**
     * to dto.
     *
     * @param user
     * @return
     */
    @Override
    UserDTO toDto(User user);
}
