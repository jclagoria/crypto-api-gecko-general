package ar.com.api.general.services;

import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.model.Search;
import ar.com.api.general.model.Trending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoSearchAPIService {

    @Value("${api.search}")
    private String URL_GECKO_SERVICE_SEARCH_API;

    @Value("${api.searchTrending}")
    private String URL_GECKO_SERVICE_SEARCH_TRENDING_API;
    private WebClient webClient;
    public CoinGeckoSearchAPIService(WebClient wClient) {
        this.webClient = wClient;
    }

    public Mono<Search> getSearchFromGeckoApi(SearchDTO filterDTO) {

        log.info("in getSearchFromGeckoApi - Calling Gecko Api Service");

        return webClient
                .get()
                .uri(URL_GECKO_SERVICE_SEARCH_API + filterDTO.getUrlFilterString())
                .retrieve()
                .bodyToMono(Search.class)
                .doOnError(throwable -> log.error("The service is unavailable!", throwable));
    }

    public Flux<Trending> getSearchTrendingFromGeckoApi(){

        log.info("in getSearTRendingFromGeckoApi - Calling Gecko Api Service");

        return webClient
                .get()
                .uri(URL_GECKO_SERVICE_SEARCH_TRENDING_API)
                .retrieve()
                .bodyToFlux(Trending.class)
                .doOnError(throwable -> log.error("The service is unavailable!", throwable));
    }

}
