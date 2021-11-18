package com.cbkim.apiserver.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String title;

    @Bean
    public Docket apiV1() {
        
    version = "V1";
    title = "CBKIM API " + version;

    Parameter parameterBuilder = new ParameterBuilder()
        .name(HttpHeaders.AUTHORIZATION)
        .description("Access Token")
        .modelRef(new ModelRef("string"))
        .parameterType("header")
        .required(false)
        .build();

    List<Parameter> globalParamters = new ArrayList<>();
    globalParamters.add(parameterBuilder);

    return new Docket(DocumentationType.SWAGGER_2)
            .globalOperationParameters(globalParamters)
            .useDefaultResponseMessages(false)
            .groupName(version)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.cbkim.apiserver.swagger.v1"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo(title, version));
    }

    private ApiInfo apiInfo(String title, String version) {
                return new ApiInfoBuilder().title(title)
                .version(version)
                .description("CBBKIM 서버 API에 대한 연동 문서입니다")
                .license("cbkim").licenseUrl("https://choongbeom.github.io/").version("1").build();
    }
}
