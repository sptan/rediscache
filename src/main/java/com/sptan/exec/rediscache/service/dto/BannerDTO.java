package com.sptan.exec.rediscache.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * Banner DTO.
 * @author lp
 * @date 2019-12-18
 */
@Data
@ApiModel("Banner")
public class BannerDTO implements Serializable {

    /**
     * id.
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * Banner名称.
     */
    @ApiModelProperty("Banner名称")
    private String bannerName;

    /**
     * 图片地址.
     */
    @ApiModelProperty("图片地址")
    private String imageUrl;

    /**
     * 展示位置.
     */
    @ApiModelProperty("展示位置")
    private String showLocation;

    /**
     * 开始展示日期.
     */
    @ApiModelProperty("开始展示日期")
    private String startDate;

    /**
     * 结束展示日期.
     */
    @ApiModelProperty("结束展示日期")
    private String endDate;

    /**
     * 类型 1:无 2: 自定义详情页, 3: 商品链接.
     */
    @ApiModelProperty("类型 1:无 2: 自定义详情页, 3: 商品链接")
    private String bannerType;

    /**
     * 商品ID.
     */
    @ApiModelProperty("商品ID")
    private String goodsId;

    /**
     * 商品名称.
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 富文本内容 类型2富文本内容3商品名称.
     */
    @ApiModelProperty("富文本内容 类型2富文本内容3商品名称")
    private String content;

    /**
     * 创建时间.
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人ID.
     */
    @ApiModelProperty("创建人ID")
    private String createUserId;

    /**
     * 创建人名称.
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 更新时间.
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人ID.
     */
    @ApiModelProperty("更新人ID")
    private String updateUserId;

    /**
     * 更新人姓名.
     */
    @ApiModelProperty("更新人姓名")
    private String updateUserName;
}