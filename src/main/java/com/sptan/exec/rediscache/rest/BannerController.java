package com.sptan.exec.rediscache.rest;

import com.sptan.exec.rediscache.domain.Banner;
import com.sptan.exec.rediscache.service.BannerService;
import com.sptan.exec.rediscache.service.dto.BannerQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Banner Controller.
 *
 * @author lp
 * @date 2019-12-18
 */
@Api(tags = "Banner管理")
@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping
    @ApiOperation("查询Banner")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String",
                    required = false, value = "Token")
    })
    public ResponseEntity getBanners(BannerQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(bannerService.queryByPage(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/queryNoCache")
    @ApiOperation("查询Banner")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String",
                    required = false, value = "Token")
    })
    public ResponseEntity getBannersNoCache(BannerQueryCriteria criteria) {
        return new ResponseEntity<>(bannerService.queryByCriteria(criteria), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("新增Banner")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String",
                    required = false, value = "Token")
    })
    public ResponseEntity create(@Validated @RequestBody Banner resources) {
        return new ResponseEntity<>(bannerService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("修改Banner")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String",
                    required = true, value = "Token")
    })
    public ResponseEntity update(@Validated @RequestBody Banner resources) {
        bannerService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("删除Banner")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String",
                    required = true, value = "Token")
    })
    public ResponseEntity delete(@PathVariable String id) {
        bannerService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/info")
    @ApiOperation("id查询Banner")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String",
                    required = false, value = "Token")
    })
    public ResponseEntity findById(@RequestParam String id) {
        return new ResponseEntity<>(bannerService.findById(id), HttpStatus.OK);
    }
}