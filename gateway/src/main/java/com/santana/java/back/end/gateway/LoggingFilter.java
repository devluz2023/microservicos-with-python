package com.santana.java.back.end.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class LoggingFilter implements GatewayFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Find the index of the second slash
        int secondSlashIndex = path.indexOf('/', 1);
        if (secondSlashIndex != -1) {
            // Extract the substring from the second slash
            String modifiedPath = path.substring(secondSlashIndex);
            logger.info("Modified path: {}", modifiedPath);
        } else {
            logger.info("Original path: {}", path);
        }

        return chain.filter(exchange);
    }
}
