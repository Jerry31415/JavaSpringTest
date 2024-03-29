package com.microservice.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.RequestHandlerSelectors;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).
                groupName("ordering-restful-api").
                apiInfo(apiInfo()).
                select().
                apis( RequestHandlerSelectors.basePackage( "com.microservice.shop.controllers" ) ).
                build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().
                contact(new Contact("Sergey Vaganov", "", "pro100-pioner@mail.ru")).
                title("Microservice for books order").
                version("0.1a").
                description("The API implementation for books order").
                build();
    }

}