package com.ffsns.sns.conguration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "FFSNS 명세서",
        description = "외국인 친구와의 화합을 위한 FFSNS API 명세서",
        version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi snsOpenApi(){
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("SNS API v1")
                .pathsToMatch(paths)
                .build();
    }
}
