package com.sptan.exec.rediscache.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author lp
 * @date 2018-11-30
 */
@Getter
@Setter
@ApiModel("APP用户认证信息")
public class AppAuthUser {

    @NotBlank
    @ApiModelProperty("手机号")
    private String mobile;

    @NotBlank
    @ApiModelProperty("密码明文")
    private String password;

    @ApiModelProperty("手机验证码")
    private String smsCode;

    private String uuid;

    @Override
    public String toString() {
        return "{mobile=" + mobile + ", password= ******}";
    }
}
