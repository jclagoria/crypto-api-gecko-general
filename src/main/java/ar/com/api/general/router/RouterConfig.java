package ar.com.api.general.router;

import ar.com.api.general.handler.GlobalApiHandler;
import ar.com.api.general.handler.HealthApiHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterConfig {

 @Value("${coins.baseURL}")
 private String URL_SERVICE_API;

 @Value("${coins.healthAPI}")
 private String URL_HEALTH_GECKO_API;

 @Value("${coins.global}")
 private String URL_GLOBAL_GECKO_API;
 
 @Bean
 public RouterFunction<ServerResponse> route(HealthApiHandler handler) {

  return RouterFunctions
            .route()
            .GET(URL_SERVICE_API + URL_HEALTH_GECKO_API, 
                        handler::getStatusServiceCoinGecko)
            .build();
 }

 @Bean
 public RouterFunction<ServerResponse> routeGlobal(GlobalApiHandler handler) {

  return RouterFunctions
          .route()
          .GET(
                  URL_SERVICE_API + URL_GLOBAL_GECKO_API,
                  handler::getGlobalDataFromGeckoApi)
          .build();
 }

}
