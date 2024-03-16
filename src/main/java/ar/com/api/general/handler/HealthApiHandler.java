package ar.com.api.general.handler;

import ar.com.api.general.enums.ErrorTypeEnum;
import ar.com.api.general.exception.ApiClientErrorException;
import ar.com.api.general.services.CoinGeckoServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class HealthApiHandler {
    private final CoinGeckoServiceStatus serviceStatus;

    public HealthApiHandler(CoinGeckoServiceStatus healthService) {
        this.serviceStatus = healthService;
    }

    public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {
        log.info("Fetching CoinGecko service status, handling request {}", serverRequest.path());

        return serviceStatus.getStatusCoinGeckoService()
                .flatMap(ping -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ping))
                .doOnSubscribe(subscription -> log.info("Retrieving status of Gecko Service"))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error -> Mono.error(
                        new ApiClientErrorException("An expected error occurred in getStatusServiceCoinGecko",
                                ErrorTypeEnum.API_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR)
                ));
    }

}