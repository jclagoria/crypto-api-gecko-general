package ar.com.api.general.handler;

import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.model.Search;
import ar.com.api.general.model.Trending;
import ar.com.api.general.services.CoinGeckoSearchAPIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class SearchApiHandler {

    private CoinGeckoSearchAPIService searchAPIService;

    public Mono<ServerResponse> getListOfCoinsWithSearchAPI(ServerRequest sRequest) {

        log.info("In SearchApiHandler.getListOfCoinsWithSearchAPI");

        SearchDTO filterDTO = SearchDTO
                .builder()
                .queryParam(sRequest.queryParam("query").get())
                .build();

        return ServerResponse
                .ok()
                .body(
                        searchAPIService.getSearchFromGeckoApi(filterDTO),
                        Search.class
                );
    }

    public Mono<ServerResponse> getTrendingOfCoinsAPI(ServerRequest serverRequest) {

        log.info("In SearchApiHandler.getTrendingOfCoinsAPI");

        return ServerResponse
                .ok()
                .body(
                        searchAPIService.getSearchTrendingFromGeckoApi(),
                        Trending.class
                );

    }

}
