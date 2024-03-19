package ar.com.api.general.handler;

import ar.com.api.general.enums.ErrorTypeEnum;
import ar.com.api.general.exception.ApiClientErrorException;
import ar.com.api.general.handler.utils.MapperHandler;
import ar.com.api.general.services.CoinGeckoSimpleApiService;
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
public class SimpleApiHandler {
    private final CoinGeckoSimpleApiService coinGeckoSimpleApiService;

    private final ValidatorOfCTOComponent validatorOfCTOComponent;

    public SimpleApiHandler(CoinGeckoSimpleApiService simpleApiService, ValidatorOfCTOComponent validatorComponent) {
        this.coinGeckoSimpleApiService = simpleApiService;
        this.validatorOfCTOComponent = validatorComponent;
    }

    public Mono<ServerResponse> getSimplePriceFromCoinGeckoApi(ServerRequest serverRequest) {
        log.info("Fetching simple price from CoinGecko API {}", serverRequest.path());

        return Mono.just(serverRequest)
                .flatMap(MapperHandler::createSimplePriceFilterDTOFromRequest)
                .flatMap(validatorOfCTOComponent::validation)
                .flatMap(coinGeckoSimpleApiService::getSimplePriceApiService)
                .flatMap(mapObject -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapObject)
                )
                .doOnSubscribe(subscription -> log.info("Retrieving Map Object from CoinGecko API"))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException(
                                "An unexpected error occurred in getSimplePriceFromCoinGeckoApi",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR)
                ));
    }

    public Mono<ServerResponse> getSimplePriceTokenByIDFromCoinGeckoApi(ServerRequest serverRequest) {
        log.info("Fetching simple price token by ID from CoinGecko API {}", serverRequest.path());

        return Mono.just(serverRequest)
                .flatMap(MapperHandler::createTokenPriceByIdDTOFromRequest)
                .flatMap(validatorOfCTOComponent::validation)
                .flatMap(coinGeckoSimpleApiService::getSimplePriceTokenById)
                .flatMap(mapObject -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapObject))
                .doOnSubscribe(subscription -> log.info("Retrieving Map Object from CoinGecko API"))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException(
                                "An unexpected error occurred in getSimplePriceTokenByIDFromCoinGeckoApi",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR)
                ));
    }

}