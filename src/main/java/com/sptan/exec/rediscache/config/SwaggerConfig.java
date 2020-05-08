package com.sptan.exec.rediscache.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * api页面 /swagger-ui.html
 *
 * @author lp
 * @date 2018-11-23
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private Boolean enabled;

    @Bean
    @SuppressWarnings("all")
    public Docket createSystemRestApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();

        return new Docket(DocumentationType.SWAGGER_2)
            .enable(enabled)
            .groupName("系统接口文档")
            .apiInfo(apiInfo("系统接口文档"))
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.sptan.exec.rediscache.rest"))
            .paths(Predicates.not(PathSelectors.regex("/error.*")))
            .build();
    }

    private ApiInfo apiInfo(String title) {
        return new ApiInfoBuilder()
            .title(title)
            .version("1.0")
            .build();
    }

}

/**
 * 将Pageable转换展示在swagger中
 */
@Configuration
class SwaggerDataConfig {

    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return newArrayList(newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)));
            }
        };
    }

    @ApiModel
    @Data
    private static class Page {
        @ApiModelProperty("页码 (0..N)")
        private Integer page;

        @ApiModelProperty("每页显示的数目")
        private Integer size;

        @ApiModelProperty("以下列格式排序标准：property[,asc | desc]。 默认排序顺序为升序。 支持多种排序条件：如：id,asc")
        private List<String> sort;
    }
}
