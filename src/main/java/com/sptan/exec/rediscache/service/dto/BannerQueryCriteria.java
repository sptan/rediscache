package com.sptan.exec.rediscache.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Banner 查询条件.
 * @author lp
 * @date 2019-12-18
 */
@Data
@ApiModel("Banner查询条件")
public class BannerQueryCriteria{

    /**
     * 模糊查询条件.
     */
    @ApiModelProperty("模糊查询条件 - Banner名称")
    private String bannerName;

    /**
     * 精确查询条件.
     */
    @ApiModelProperty("精确查询条件 - 展示位置")
    private String showLocation;
}