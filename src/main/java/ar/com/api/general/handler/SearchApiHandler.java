package ar.com.api.general.handler;

import ar.com.api.general.enums.ErrorTypeEnum;
import ar.com.api.general.exception.ApiClientErrorException;
import ar.com.api.general.handler.utils.MapperHandler;
import ar.com.api.general.services.CoinGeckoSearchAPIService;
import ar.com.api.general.validator.ValidatorOfCTOComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SearchApiHandler {

    private final CoinGeckoSearchAPIService searchAPIService;

    private final ValidatorOfCTOComponent validatorOfCTOComponent;

    public SearchApiHandler(CoinGeckoSearchAPIService sApiService, ValidatorOfCTOComponent validatorComponent) {
        this.searchAPIService = sApiService;
        this.validatorOfCTOComponent = validatorComponent;
    }

    public Mono<ServerResponse> getListOfCoinsWithSearchAPI(ServerRequest sRequest) {
        log.info("In Search -> getListOfCoinsWithSearchAPI, handling request {}", sRequest.path());

        return Mono.just(sRequest)
                .flatMap(MapperHandler::createSearchDTOFromRequest)
                .flatMap(validatorOfCTOComponent::validation)
                .flatMap(searchAPIService::getSearchFromGeckoApi)
                .flatMap(searchResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(searchResponse)
                )
                .doOnSubscribe(subscription -> log.info("Retrieving Search from CoinGecko Api"))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException(
                                "An unexpected error occurred in getListOfCoinsWithSearchAPI",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )
                ));

    }

    public Mono<ServerResponse> getTrendingOfCoinsAPI(ServerRequest sRequest) {
        log.info("In Search -> getTrendingOfCoinsAPI, handling request {}", sRequest.path());

        return searchAPIService.getSearchTrendingFromGeckoApi()
                .flatMap(trending ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(trending))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSubscribe(subscription -> log.info("Retrieving Trending form CoinGecko API"))
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException("An unexpected error occurred in getTrendingOfCoinsAPI",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR)
                ));
    }

}