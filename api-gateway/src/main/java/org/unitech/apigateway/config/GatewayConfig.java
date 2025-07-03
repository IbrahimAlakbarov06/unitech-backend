package org.unitech.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.unitech.apigateway.filter.AuthFilter;

@Component
public class GatewayConfig {

    @Autowired
    private AuthFilter authFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("lb://ms-auth"))

                .route("account-service", r -> r
                        .path("/api/accounts/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("lb://ms-account"))

                .route("transfer-service", r -> r
                        .path("/api/transfers/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("lb://ms-transfer"))

                .route("currency-service", r -> r
                        .path("/api/currency/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("lb://ms-currency"))

                .route("bff-service", r -> r
                        .path("/api/bff/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("lb://ms-bff"))

                .build();
    }
}
