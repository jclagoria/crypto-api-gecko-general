package ar.com.api.general.handler;

import ar.com.api.general.model.ExchangeRate;
import ar.com.api.general.services.CoinGeckoServiceExchangeRatesApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class ExchangeRateApiHandler {

    private CoinGeckoServiceExchangeRatesApi exchangeRatesApi;

    public Mono<ServerResponse> getExchangeRateFromGeckoApi(ServerRequest serverRequest) {

        log.info("In ExchangeRateApiHandler.getExchangeRateFromGeckoApi");

        return ServerResponse
                .ok()
                .body(
                        exchangeRatesApi.getExchangeRatesFromGeckoApi(),
                        ExchangeRate.class
                );

    }

}
