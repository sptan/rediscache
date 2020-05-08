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
@ApiModel("APP用户注册DTO")
public class AppRegisterDTO {

    @NotBlank
    @ApiModelProperty("11位手机号")
    private String mobile;

    @NotBlank
    @ApiModelProperty("6-20位密码,明文")
    private String password;

    @ApiModelProperty("图形验证码")
    private String captcha;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("生日,格式 yyyy-MM-dd")
    private String birthday;

    @ApiModelProperty("openid,如果openid不为空,需要进行手机号和openid账号融合")
    private String openid;

    @ApiModelProperty("获取图形验证码时返回的uuid")
    private String uuid;

    @Override
    public String toString() {
        return "{mobile=" + mobile + ", password= ******}";
    }
}
