package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.model.DecentralizedFinance;
import ar.com.api.general.model.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
@Slf4j
public class CoinGeckoGeneralServicesApi extends CoinGeckoServiceApi {
    private ExternalServerConfig externalServerConfig;
    private WebClient webClient;
    public CoinGeckoGeneralServicesApi(WebClient webClient, ExternalServerConfig externalServerConfig) {
        this.webClient = webClient;
        this.externalServerConfig = externalServerConfig;
    }
    public Mono<Global> getGlobalData() {

        log.info("in getGlobalData - Calling Gecko Api Service -> " + externalServerConfig.getGlobal());

        return webClient
                .get()
                .uri(externalServerConfig.getGlobal())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoServerException()
                )
                .bodyToMono(Global.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

    public Mono<DecentralizedFinance> getDecentralizedFinance() {
        log.info("getGlobalData - Calling DecentralizedFinance -> " + externalServerConfig.getDecentralized());

        return webClient
                .get()
                .uri(externalServerConfig.getDecentralized())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoServerException()
                )
                .bodyToMono(DecentralizedFinance.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

}
