package com.codigo.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Verificar si la ruta necesita autenticación
        String path = request.getPath().value();
        if (path.contains("/auth/")) {
            return chain.filter(exchange);
        }

        // Verificar token
        List<String> authHeader = request.getHeaders().get("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
        }

        // Continuar con la cadena de filtros
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
