package com.codigo.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms-security-route", r -> r
                        .path("/api/security/**")
                        .filters(f -> f
                                .rewritePath("/api/security/(?<segment>.*)",
                                        "/api/security/${segment}")
                                .addRequestHeader("X-Gateway-Request", "true")
                                .addResponseHeader("X-Response-Time",
                                        LocalDateTime.now().toString()))
                        .uri("lb://ms-security"))
                .build();
    }
}