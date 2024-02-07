package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.dto.SimplePriceFilterDTO;
import ar.com.api.general.dto.TokenPriceByIdDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
public class CoinGeckoSimpleApiService extends CoinGeckoServiceApi {

    private ExternalServerConfig externalServerConfig;
    private WebClient webClient;
    public CoinGeckoSimpleApiService(WebClient wClient, ExternalServerConfig externalServerConfig) {
        this.webClient = wClient;
        this.externalServerConfig = externalServerConfig;
    }

    public Mono<Map> getSimplePriceApiService(SimplePriceFilterDTO filterDTO) {

        log.info("in getSimplePriceApiService - Calling Gecko Api Service -> "
                + externalServerConfig.getSimplePrice()
                + filterDTO.getUrlFilterString());

        return webClient
                .get()
                .uri(externalServerConfig.getSimplePrice() + filterDTO.getUrlFilterString())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoDataException()
                )
                .bodyToMono(Map.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

    public Mono<Map> getSimplePriceTokenById(TokenPriceByIdDTO filterDTO){

        log.info("in getSimplePriceTokenById - Calling Gecko Api Service -> " +
                String.format(externalServerConfig.getSimpleTokePriceById(),
                        filterDTO.getIds())
                + filterDTO.getUrlFilterString());

        return webClient
                .get()
                .uri(
                        String.format(
                                externalServerConfig.getSimpleTokePriceById(),
                                filterDTO.getIds()) + filterDTO.getUrlFilterString()
                ).retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoDataException()
                )
                .bodyToMono(Map.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }



}
