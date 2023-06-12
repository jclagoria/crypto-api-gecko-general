package ar.com.api.general.services;

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

    @Value("${api.exchangeRates}")
    private String URL_GECKO_SERVICE_EXCHANGE_RATE_API;

    public CoinGeckoServiceExchangeRatesApi(WebClient webClient) {
        this.wClient = webClient;
    }
    public Mono<ExchangeRate> getExchangeRatesFromGeckoApi() {

        log.info("in getExchangeRatesFromGeckoApi - Calling Gecko Api Service");

        return wClient
                .get()
                .uri(URL_GECKO_SERVICE_EXCHANGE_RATE_API)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        getClientResponseMonoDataException()
                )
                .bodyToMono(ExchangeRate.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

}
