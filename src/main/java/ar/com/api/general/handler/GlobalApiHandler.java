package ar.com.api.general.handler;

import ar.com.api.general.enums.ErrorTypeEnum;
import ar.com.api.general.exception.ApiClientErrorException;
import ar.com.api.general.services.CoinGeckoGeneralServicesApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class GlobalApiHandler {
    private final CoinGeckoGeneralServicesApi generalService;
    public GlobalApiHandler(CoinGeckoGeneralServicesApi generalService) {
        this.generalService = generalService;
    }
    public Mono<ServerResponse> getGlobalDataFromGeckoApi(ServerRequest serverRequest){
        log.info("Fetching global data from CoinGecko API {}", serverRequest.path());

        return generalService.getGlobalData()
                .flatMap(data -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data))
                .doOnSubscribe(subscription -> log.info("Retrieving Global Data from CoinGecko Api"))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException("An unexpected error occurred in getGlobalDataFromGeckoApi",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR)
                ));
    }
    public Mono<ServerResponse> getDecentralizedFinanceDefi(ServerRequest sRequest) {
        log.info("Fetching decentralized finance data from CoinGecko API {}", sRequest.path());

        return generalService.getDecentralizedFinance()
                .flatMap(defi -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(defi))
                .doOnSubscribe(subscription -> log.info("Retrieve Decentralized Finance from CoinGecko API"))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException("An unexpected error occurred in getDecentralizedFinanceDefi",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR)
                ));
    }

}