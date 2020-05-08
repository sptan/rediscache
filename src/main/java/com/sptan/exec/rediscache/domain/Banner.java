package com.sptan.exec.rediscache.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Banner Entity.
 *
 * @author lp
 * @date 2019-12-18
 */
@Entity
@Data
@Table(name = "banner")
@ApiModel("Banner")
@DynamicUpdate
public class Banner implements Serializable {

    public static final String SHOW_LOCATION_WEB = "1";

    public static final String SHOW_LOCATION_APP = "2";

    /**
     * id.
     */
    @ApiModelProperty("id")
    @Id
    @GenericGenerator(name = "sequence", strategy = "com.sptan.exec.framework.identifier.SnowflakeIdGenerator")
    @GeneratedValue(generator = "sequence")
    @Column(name = "id")
    private String id;

    /**
     * Banner名称.
     */
    @ApiModelProperty("Banner名称")
    @Column(name = "banner_name", nullable = false)
    private String bannerName;

    /**
     * 图片地址.
     */
    @ApiModelProperty("图片地址")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    /**
     * 展示位置.
     */
    @ApiModelProperty("展示位置")
    @Column(name = "show_location", nullable = false)
    private String showLocation;

    /**
     * 开始展示日期.
     */
    @ApiModelProperty("开始展示日期")
    @Column(name = "start_date")
    private Date startDate;

    /**
     * 结束展示日期.
     */
    @ApiModelProperty("结束展示日期")
    @Column(name = "end_date")
    private Date endDate;

    /**
     * 类型 1:无 2: 自定义详情页, 3: 商品链接.
     */
    @ApiModelProperty("类型 1:无 2: 自定义详情页, 3: 商品链接")
    @Column(name = "banner_type")
    private Integer bannerType;

    /**
     * 商品ID.
     */
    @ApiModelProperty("商品ID")
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 商品名称.
     */
    @ApiModelProperty("商品名称")
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 富文本内容 类型2富文本内容3商品名称.
     */
    @ApiModelProperty("富文本内容 类型2富文本内容3商品名称")
    @Column(name = "content")
    private String content;

    /**
     * 创建时间.
     */
    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人ID.
     */
    @ApiModelProperty("创建人ID")
    @Column(name = "create_user_id")
    private String createUserId;

    /**
     * 创建人名称.
     */
    @ApiModelProperty("创建人名称")
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 更新时间.
     */
    @ApiModelProperty("更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新人ID.
     */
    @ApiModelProperty("更新人ID")
    @Column(name = "update_user_id")
    private String updateUserId;

    /**
     * 更新人姓名.
     */
    @ApiModelProperty("更新人姓名")
    @Column(name = "update_user_name")
    private String updateUserName;

    public void copy(Banner source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}