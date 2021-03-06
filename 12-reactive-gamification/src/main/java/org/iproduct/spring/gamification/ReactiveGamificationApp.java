package org.iproduct.spring.gamification;

import org.iproduct.spring.gamification.handlers.ReactiveGameHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
public class ReactiveGamificationApp {

    @Bean
    public RouterFunction<ServerResponse> routes(ReactiveGameHandler gameHandler) {

        return route(GET("/api/game").and(accept(APPLICATION_STREAM_JSON)), gameHandler::streamGameObjects)
                .andRoute(GET("/api/game").and(accept(MediaType.TEXT_EVENT_STREAM)), gameHandler::streamGameObjectsSSE);

    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveGamificationApp.class, args);
    }

}

