package com.sptan.exec.rediscache.service.impl;


import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.sptan.exec.rediscache.domain.Banner;
import com.sptan.exec.rediscache.repository.BannerRepository;
import com.sptan.exec.rediscache.service.BannerService;
import com.sptan.exec.rediscache.service.dto.BannerDTO;
import com.sptan.exec.rediscache.service.dto.BannerQueryCriteria;
import com.sptan.exec.rediscache.service.mapper.BannerMapper;
import com.sptan.exec.rediscache.utils.DateUtils;
import com.sptan.exec.rediscache.utils.PageUtil;
import com.sptan.exec.rediscache.utils.QueryHelp;
import com.sptan.exec.rediscache.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Banner 业务接口实现.
 *
 * @author lp
 * @date 2019-12-18
 */
@Service
@CacheConfig(cacheNames = "bannerClass")
@Transactional(rollbackFor = Exception.class)
public class BannerServiceImpl implements BannerService {

    /**
     * 首页显示的最大banner数量.
     */
    private static final long APP_BANNER_SIZE = 5;

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    @Cacheable(value = "hashCodeKey", key = "'key:p0:' + #p0.hashCode() + 'p1:' + #p1.hashCode()")
    public Map<String, Object> queryByPage(BannerQueryCriteria criteria, Pageable pageable) {
        Page<Banner> page =
                bannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,
                        criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(bannerMapper::toDto));
    }

    @Override
    @Cacheable("queryByCriteria")
    public List<BannerDTO> queryByCriteria(BannerQueryCriteria criteria) {
        return bannerMapper.toDto(bannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Cacheable(value = "banner", key = "#p0")
    public BannerDTO findById(String id) {
        Banner banner = bannerRepository.findById(id).orElseGet(Banner::new);
        ValidationUtil.isNull(banner.getId(), "Banner", "id", id);
        return bannerMapper.toDto(banner);
    }

    @Override
    @CachePut(value = "bannerPrimaryKey", key = "#result.getId()")
    @Transactional(rollbackFor = Exception.class)
    public BannerDTO create(Banner resources) {
        resources.setId(IdUtil.simpleUUID());
        return bannerMapper.toDto(bannerRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Banner resources) {
        Banner banner = bannerRepository.findById(resources.getId()).orElseGet(Banner::new);
        ValidationUtil.isNull(banner.getId(), "Banner", "id", resources.getId());
        banner.copy(resources);
        bannerRepository.save(banner);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        bannerRepository.deleteById(id);
    }

    /**
     * 获取app首页使用的banners,最多显示5个, 按照更新时间倒序.
     *
     * @return the app banners
     */
    @Override
    public List<BannerDTO> getBanners(String bannerType) {
        Date nowTime = new Date();
        List<Banner> allBanners = bannerRepository.findAll(Sort.by(Sort.Direction.DESC, "updateTime"));
        List<Banner> collect = allBanners.stream()
                .filter(e -> bannerType.equals(e.getShowLocation()) && DateUtils.isEffectiveDate(nowTime, e.getStartDate(), e.getEndDate()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return Lists.newArrayList();
        }
        return collect.stream().limit(APP_BANNER_SIZE)
                .map(entity -> bannerMapper.toDto(entity)).collect(Collectors.toList());
    }


}