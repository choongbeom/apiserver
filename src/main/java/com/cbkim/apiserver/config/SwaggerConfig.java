package com.cbkim.apiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String title;

    @Bean
    public Docket swaggerApi() {
        
    version = "1.0";
    title = "CBKIM API";

    return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.cbkim.apiserver.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo(title, version));
    }

    private ApiInfo apiInfo(String title, String version) {
                return new ApiInfoBuilder().title(title)
                .description("CBBKIM 서버 API에 대한 연동 문서입니다")
                .license("cbkim").licenseUrl("https://choongbeom.github.io/").version(version).build();
    }
}
