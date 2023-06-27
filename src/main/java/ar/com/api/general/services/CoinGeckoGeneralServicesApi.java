package ar.com.api.general.services;

import ar.com.api.general.model.DecentralizedFinance;
import ar.com.api.general.model.Global;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoGeneralServicesApi extends CoinGeckoServiceApi {

    @Value("${api.global}")
    private String URL_GECKO_SERVICE_GLOBAL_API;

    @Value("${api.decentralized}")
    private String URL_GECKO_SERVICE_DECENTRALIZED_FINANCE_DEFI;
    private WebClient webClient;

    public CoinGeckoGeneralServicesApi(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<Global> getGlobalData() {

        log.info("in getGlobalData - Calling Gecko Api Service");

        return webClient
                .get()
                .uri(URL_GECKO_SERVICE_GLOBAL_API)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        getClientResponseMonoServerException()
                )
                .bodyToMono(Global.class)/*
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                )*/;
    }

    public Mono<DecentralizedFinance> getDecentralizedFinance() {

        log.info("n getGlobalData - Calling DecentralizedFinance");

        return webClient
                .get()
                .uri(URL_GECKO_SERVICE_DECENTRALIZED_FINANCE_DEFI)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        getClientResponseMonoServerException()
                )
                .bodyToMono(DecentralizedFinance.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

}
