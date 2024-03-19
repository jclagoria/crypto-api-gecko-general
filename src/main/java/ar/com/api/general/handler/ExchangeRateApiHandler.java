package ar.com.api.general.handler;

import ar.com.api.general.enums.ErrorTypeEnum;
import ar.com.api.general.exception.ApiClientErrorException;
import ar.com.api.general.services.CoinGeckoServiceExchangeRatesApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ExchangeRateApiHandler {
    private final CoinGeckoServiceExchangeRatesApi exchangeRatesApi;

    public ExchangeRateApiHandler(CoinGeckoServiceExchangeRatesApi apiService) {
        this.exchangeRatesApi = apiService;
    }

    public Mono<ServerResponse> getExchangeRateFromGeckoApi(ServerRequest serverRequest) {
        log.info("Fetching exchange rate from CoinGecko API");

        return exchangeRatesApi.getExchangeRatesFromGeckoApi()
                .flatMap(rate -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(rate))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSubscribe(subscription -> log.info("Retrieving Exchange Rate from CoinGecko Api"))
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException(
                                "An unexpected error occurred in getExchangeRateFromGeckoApi",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        ))
                );
    }

}
