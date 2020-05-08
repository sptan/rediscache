package com.sptan.exec.rediscache.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lp
 * @date 2020-02-06
 */
@Data
@ApiModel("APP用户根据旧密码修改密码DTO")
public class AppChangePasswordFromOldPswDTO {

    @NotBlank
    @ApiModelProperty("新密码明文")
    private String newPassword;

    @ApiModelProperty("旧密码明文")
    private String oldPassword;

}
