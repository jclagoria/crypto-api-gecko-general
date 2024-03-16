package ar.com.api.general.router;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.handler.SimpleApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SimpleRouteConfig {
    private ApiServiceConfig apiServiceConfig;

    public SimpleRouteConfig(ApiServiceConfig apiServiceConfig) {
        this.apiServiceConfig = apiServiceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeSimple(SimpleApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(apiServiceConfig.getBaseURL() + apiServiceConfig.getSimplePrice(),
                        handler::getSimplePriceFromCoinGeckoApi)
                .GET(apiServiceConfig.getBaseURL() + apiServiceConfig.getSimpleTokePriceById(),
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        handler::getSimplePriceTokenByIDFromCoinGeckoApi)
                .build();

    }

}
