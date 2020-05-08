package com.sptan.exec.rediscache.service;

import com.sptan.exec.rediscache.domain.Banner;
import com.sptan.exec.rediscache.service.dto.BannerDTO;
import com.sptan.exec.rediscache.service.dto.BannerQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Banner 业务接口定义.
 *
 * @author lp
 * @date 2019 -12-18
 */
public interface BannerService {

    /**
     * 查询数据分页.
     *
     * @param criteria 条件参数
     * @param pageable 分页参数
     * @return Map<String, Object> map
     */
    Map<String, Object> queryByPage(BannerQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页.
     *
     * @param criteria 条件参数
     * @return List<BannerDTO> list
     */
    List<BannerDTO> queryByCriteria(BannerQueryCriteria criteria);

    /**
     * 根据ID查询.
     *
     * @param id ID
     * @return BannerDTO banner dto
     */
    BannerDTO findById(String id);

    /**
     * Create Banner dto.
     *
     * @param resources the resources
     * @return the className dto
     */
    BannerDTO create(Banner resources);

    /**
     * 更新Banner数据对象.
     *
     * @param resources the resources
     */
    void update(Banner resources);

    /**
     * 根据主键删除.
     *
     * @param id id
     */
    void delete(String id);

    /**
     * 获取app首页使用的banners,最多显示5个, 按照更新时间倒序.
     *
     * @param bannerType
     * @return the app banners
     */
    List<BannerDTO> getBanners(String bannerType);
}