package com.tom.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@ConditionalOnExpression(value = "${useSwagger:false}")
public class SwaggerConfig {

    /* controller  的 package 名 */
    static final String basePackage = "com.tom.api.controller";

    /* 文件title */
    static final String docTitle = "Tom APIs 說明";

    /* 作者信箱 */
    final static String mail = "benny139@gmail.com";

    /* 版号 */
    final static String version = "1.0.0";

    /* 開發團隊名稱 */
    final static String devTeamName = "開發團隊名稱";

    @Bean
    public Docket createRestApi() {
        //=====添加head参数start============================
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("oToken").description("Access Token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        // =========添加head参数end===================

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())//paths(PathSelectors.regex("/.*"))
                .build()
                .globalOperationParameters(pars);//************把消息头添加
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(docTitle)
                .version(version)
                .contact(new Contact("Dev-Team", "", mail))
                .build();
    }
}
