package ar.com.api.general.router;

import ar.com.api.general.exception.CoinGeckoDataNotFoudException;
import ar.com.api.general.handler.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


@Configuration
public class  RouterConfig {

 @Value("${coins.baseURL}")
 private String URL_SERVICE_API;

 @Value("${coins.exchangeRates}")
 private String URL_EXCHANGE_RATE_GECKO_API;

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
 public WebExceptionHandler exceptionHandler() {
  return (ServerWebExchange exchange, Throwable ex) -> {
   if (ex instanceof CoinGeckoDataNotFoudException) {
    exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
    return exchange.getResponse().setComplete();
   }
   return Mono.error(ex);
  };
 }

}
