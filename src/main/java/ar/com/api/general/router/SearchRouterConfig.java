package ar.com.api.general.router;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.handler.SearchApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SearchRouterConfig extends AbstractRouterConfig {
    private ApiServiceConfig apiServiceConfig;

    public SearchRouterConfig(ApiServiceConfig apiServiceConfig) {
        this.apiServiceConfig = apiServiceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeSearch(SearchApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(
                        apiServiceConfig.getBaseURL() + apiServiceConfig.getSearch(),
                        handler::getListOfCoinsWithSearchAPI
                )
                .GET(
                        apiServiceConfig.getBaseURL() + apiServiceConfig.getSearchTrending(),
                        handler::getTrendingOfCoinsAPI
                )
                .build();
    }

}
