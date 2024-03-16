package ar.com.api.general.router;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.handler.ExchangeRateApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ExchangeRateRouterConfig {
    private ApiServiceConfig apiServiceConfig;

    public ExchangeRateRouterConfig(ApiServiceConfig serviceConfig) {
        this.apiServiceConfig = serviceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeExchangeRate(ExchangeRateApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(
                        apiServiceConfig.getBaseURL() + apiServiceConfig.getExchangeRates(),
                        handler::getExchangeRateFromGeckoApi
                )
                .build();
    }

}
