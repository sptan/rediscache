package com.sptan.exec.rediscache.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lp
 * @date 2020-02-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("APP用户修改手机号DTO")
public class AppChangeMobileDTO {

    @NotBlank
    @ApiModelProperty("新手机号")
    private String newMobile;

    @ApiModelProperty("手机验证码")
    private String smsCode;

}
