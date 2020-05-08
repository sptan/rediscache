package com.sptan.exec.rediscache.service.mapper;

import com.sptan.exec.rediscache.base.BaseMapper;
import com.sptan.exec.rediscache.domain.Banner;
import com.sptan.exec.rediscache.service.dto.BannerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Banner Mapper.
 * @author lp
 * @date 2019-12-18
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BannerMapper extends BaseMapper<BannerDTO, Banner> {

}