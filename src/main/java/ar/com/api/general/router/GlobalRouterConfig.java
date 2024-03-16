package ar.com.api.general.router;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.handler.GlobalApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GlobalRouterConfig {
    private ApiServiceConfig apiServiceConfig;

    public GlobalRouterConfig(ApiServiceConfig apiServiceConfig) {
        this.apiServiceConfig = apiServiceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeGlobal(GlobalApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(
                        apiServiceConfig.getBaseURL() + apiServiceConfig.getGlobal(),
                        handler::getGlobalDataFromGeckoApi
                )
                .GET(
                        apiServiceConfig.getBaseURL() + apiServiceConfig.getDecentralized(),
                        handler::getDecentralizedFinanceDefi
                )
                .build();
    }

}
