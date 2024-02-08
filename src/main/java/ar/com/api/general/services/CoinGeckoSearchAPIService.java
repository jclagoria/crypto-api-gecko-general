package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.model.Search;
import ar.com.api.general.model.Trending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoSearchAPIService extends CoinGeckoServiceApi {

    private ExternalServerConfig externalServerConfig;
    private WebClient webClient;
    public CoinGeckoSearchAPIService(WebClient wClient, ExternalServerConfig externalServerConfig) {
        this.webClient = wClient;
        this.externalServerConfig = externalServerConfig;
    }

    public Mono<Search> getSearchFromGeckoApi(SearchDTO filterDTO) {

        log.info("in getSearchFromGeckoApi - Calling Gecko Api Service -> "
                + externalServerConfig.getSearch()
                + filterDTO.getUrlFilterString());

        return webClient
                .get()
                .uri(externalServerConfig.getSearch()
                        + filterDTO.getUrlFilterString())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoDataException()
                )
                .bodyToMono(Search.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

    public Mono<Trending> getSearchTrendingFromGeckoApi(){

        log.info("in getSearTRendingFromGeckoApi - Calling Gecko Api Service -> "
                + externalServerConfig.getSearchTrending());

        return webClient
                .get()
                .uri(externalServerConfig.getSearchTrending())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        getClientResponseMonoDataException()
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        getClientResponseMonoDataException()
                )
                .bodyToMono(Trending.class)
                .doOnError(
                        ManageExceptionCoinGeckoServiceApi::throwServiceException
                );
    }

}
