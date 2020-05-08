package com.sptan.exec.rediscache.repository;

import com.sptan.exec.rediscache.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Banner 数据访问层.
 * @author lp
 * @date 2019-12-18
 */
public interface BannerRepository extends JpaRepository<Banner, String>, JpaSpecificationExecutor<Banner> {
}