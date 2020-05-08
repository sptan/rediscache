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
@ApiModel("APP用户修改密码DTO")
public class AppChangePasswordDTO {

    @NotBlank
    @ApiModelProperty("手机号")
    private String mobile;

    @NotBlank
    @ApiModelProperty("密码明文")
    private String newPassword;

    @ApiModelProperty("手机验证码")
    private String smsCode;

}
