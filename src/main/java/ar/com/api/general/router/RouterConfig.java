package ar.com.api.general.router;

import ar.com.api.general.handler.*;
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

 @Value("${coins.decentralized}")
 private String URL_DECENTRALIZED_GECKO_API;

 @Value("${coins.exchangeRates}")
 private String URL_EXCHANGE_RATE_GECKO_API;

 @Value("${coins.search}")
 private String URL_SEARCH_GECKO_API;

 @Value("${coins.simplePrice}")
 private String URL_SIMPLE_PRICE_GECKO_API;

 @Value("${coins.simpleTokePriceById}")
 private String URL_SIMPLE_TOKEN_PRICE_BY_ID;

 @Value("${coins.searchTrending}")
 private String URL_SEARCH_TRENDING_COIN_API;

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
                  handler::getGlobalDataFromGeckoApi
          )
          .GET(
                  URL_SERVICE_API + URL_DECENTRALIZED_GECKO_API,
                  handler::getDecentralizedFinanceDefi
          )
          .build();
 }

 @Bean
 public RouterFunction<ServerResponse> routeExchangeRate(ExchangeRateApiHandler handler) {

  return RouterFunctions
          .route()
          .GET(
                  URL_SERVICE_API + URL_EXCHANGE_RATE_GECKO_API,
                  handler::getExchangeRateFromGeckoApi
          )
          .build();
 }

 @Bean
 public RouterFunction<ServerResponse> routeSearch(SearchApiHandler handler) {

  return RouterFunctions
          .route()
          .GET(
                  URL_SERVICE_API + URL_SEARCH_GECKO_API,
                  handler::getListOfCoinsWithSearchAPI
          )
          .GET(
                  URL_SERVICE_API + URL_SEARCH_TRENDING_COIN_API,
                  handler::getTrendingOfCoinsAPI
          )
          .build();

 }
 @Bean
 public RouterFunction<ServerResponse> routeSimple(SimpleApiHandler handler) {

  return RouterFunctions
          .route()
          .GET(URL_SERVICE_API + URL_SIMPLE_PRICE_GECKO_API,
                  handler::getSimplePriceFromCoinGeckoApi)
          .GET(URL_SERVICE_API + URL_SIMPLE_TOKEN_PRICE_BY_ID,
                  handler::getSimplePriceTokenByIDFromCoinGeckoApi)
          .build();

 }

}
