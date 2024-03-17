package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.configuration.HttpServiceCall;
import ar.com.api.general.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceExchangeRatesApi {
    private final HttpServiceCall httpServiceCall;
    private final ExternalServerConfig externalServerConfig;

    public CoinGeckoServiceExchangeRatesApi(HttpServiceCall serviceCall, ExternalServerConfig externalServerConfig) {
        this.httpServiceCall = serviceCall;
        this.externalServerConfig = externalServerConfig;
    }

    public Mono<ExchangeRate> getExchangeRatesFromGeckoApi() {

        log.info("in getExchangeRatesFromGeckoApi - Calling Gecko Api Service -> "
                + externalServerConfig.getExchangeRates());

        return httpServiceCall.getMonoObject(externalServerConfig.getExchangeRates(), ExchangeRate.class);
    }

}
