package com.appsdeveloperblog.app.ws.mobileappws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocket()
    {
        Contact contact = new Contact(
            "Edward Willie",
             "http://www.appsdeveloperblog.com",
              "developer@appsdeveloperblog.com");

        List<VendorExtension> vendorExtensions = new ArrayList<>();
        
        ApiInfo apiInfo = new ApiInfo(
            "Photo app Restful web service documentatiom",
             "This pages documents Photo app RESTful Web Service",
              "1.0",
               "http://wwww.appsdeveloperblog.com/service.html",
                contact,
                 "Apache 2.0",
                  "http://www.apache.org/licenses/LICENSE-2.0",
                   vendorExtensions);


        Docket docket = new Docket(DocumentationType.SWAGGER_2)
        .protocols(new HashSet<>(Arrays.asList("HTTP", "HTTPS")))
        .apiInfo(apiInfo)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.appsdeveloperblog.app.ws.mobileappws"))
        .paths(PathSelectors.any())
        .build();

        return docket;
    }

}