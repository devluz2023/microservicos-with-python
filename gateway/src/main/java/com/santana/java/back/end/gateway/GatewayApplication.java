package com.santana.java.back.end.gateway;

import com.santana.java.back.end.gateway.LoggingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("app-sender_route", r -> r.path("/app-sender/**")
//                        .uri("http://app-sender:80"))
//                .route("app-receiver_route", r -> r.path("/app-receiver/**")
//                        .uri("http://app-receiver:80"))
//                .route("app-orchestrating_route", r -> r.path("/app-orchestrating/**")
//                        .uri("http://app-orchestrating:80"))
//                .route("openapi_route", r -> r.path("/internal-application-gateway/app-sender/docs")
//                        .filters(f -> f.rewritePath("/internal-application-gateway/app-sender/docs", "/openapi.json"))
//                        .uri("http://a4490d42fea3c43b98e9275552137390-487069486.sa-east-1.elb.amazonaws.com"))
//                .build();
//    }

//    RouteLocatorBuilder.Builder apiDocUIRoutes(RouteLocatorBuilder.Builder builder) {
//        return builder
//                .route(p -> swaggerUi(p, "microservice1", "/microservice1-api/swagger-config"))
//                .route(p -> swaggerUi(p, "microservice2", "/microservice2-api/swagger-config"))

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        LoggingFilter loggingFilter = new LoggingFilter();

        return builder.routes()
                // Route for Swagger JSON of app-sender
                .route("app-sender-api_route", r -> r.path("/app-sender-api/**")
                        .uri("http://localhost:8000"))
                // Route for Swagger UI of app-sender
                .route("app-sender-swagger-ui_route", r -> r.path("/app-sender/api.html")
                        .uri("http://localhost:8000"))
                // General route for other app-sender endpoints
                .route("app-sender_route", r -> r.path("/app-sender/**")
                        .filters(f -> f.rewritePath("/app-sender/(?<remaining>.*)", "/${remaining}")
                                .filter(loggingFilter))
                        .uri("http://localhost:8000"))
                // ... Add similar configuration for other routes
                .build();
    }


    private Buildable<Route> swaggerUi(PredicateSpec p, String service, String expectedValue) {
        return p.path("/swagger-ui/index.html").and().query("configUrl", expectedValue)
                .uri(service);
    }
//    @Bean
//    public RouterFunction<ServerResponse> staticRouter() {
//        return route(RequestPredicates.GET("/openapi.json"),
//                request -> {
//                    BodyBuilder response = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON);
//                    return response.bodyValue("Your OpenAPI JSON Content Here");
//                });
//    }
}
