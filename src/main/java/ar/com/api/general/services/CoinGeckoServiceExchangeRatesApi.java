package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceExchangeRatesApi extends CoinGeckoServiceApi {
    private WebClient wClient;
    private ExternalServerConfig externalServerConfig;

    public CoinGeckoServiceExchangeRatesApi(WebClient webClient, ExternalServerConfig externalServerConfig) {
        this.wClient = webClient;
        this.externalServerConfig = externalServerConfig;
    }
    public Mono<ExchangeRate> getExchangeRatesFromGeckoApi() {

        log.info("in getExchangeRatesFromGeckoApi - Calling Gecko Api Service -> "
                + externalServerConfig.getExchangeRates());

        return wClient
                .get()
                .uri(externalServerConfig.getExchangeRates())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoDataException()
                )
                .bodyToMono(ExchangeRate.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

}
