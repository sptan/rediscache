package com.sptan.exec.framework.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liupeng
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("APP会员")
public class MemberProfile extends UserProfile {

    public static final String USER_TYPE_APP = "APP";

    /**
     * 用户类型.
     */
    @ApiModelProperty("用户类型")
    private String userType = USER_TYPE_APP;

    /**
     * 用户ID.
     */
    @ApiModelProperty("会员ID")
    private String memberId;

    /**
     * 手机号.
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * APP微信openid.
     */
    @ApiModelProperty("APP微信openid")
    private String appOpenid;

    /**
     * 小程序微信openid.
     */
    @ApiModelProperty("小程序微信openid")
    private String miniOpenid;

    /**
     * unionId.
     */
    @ApiModelProperty("微信unionId")
    private String unionid;

    public static MemberProfile fromMobile(String memberId, String mobile) {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(memberId);
        memberProfile.setMemberId(memberId);
        memberProfile.setUserType(USER_TYPE_APP);
        memberProfile.setMobile(mobile);
        return memberProfile;
    }

    public static MemberProfile fromUnionid(String memberId, String unionid) {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setUserId(memberId);
        memberProfile.setMemberId(memberId);
        memberProfile.setUserType(USER_TYPE_APP);
        memberProfile.setUnionid(unionid);
        return memberProfile;
    }
}
