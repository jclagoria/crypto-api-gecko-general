package ar.com.api.general.handler;

import ar.com.api.general.services.CoinGeckoServiceExchangeRatesApi;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class ExchangeRateApiHandler {
    private CoinGeckoServiceExchangeRatesApi exchangeRatesApi;
    public ExchangeRateApiHandler(CoinGeckoServiceExchangeRatesApi apiService) {
        this.exchangeRatesApi = apiService;
    }
    public Mono<ServerResponse> getExchangeRateFromGeckoApi(ServerRequest serverRequest) {
        log.info("Fetching exchange rate from CoinGecko API");

        return exchangeRatesApi.getExchangeRatesFromGeckoApi()
                .flatMap(rate ->
                        ServerResponse.ok().bodyValue(rate))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> {
                    log.error("Error fetching exchange rates", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Failed to fetch exchange rates due to an internal error.");
                });
    }

}
