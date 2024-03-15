package ar.com.api.general.handler;

import ar.com.api.general.exception.CoinGeckoDataNotFoudException;
import ar.com.api.general.services.CoinGeckoGeneralServicesApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
@Slf4j
public class GlobalApiHandler {
    private CoinGeckoGeneralServicesApi generalService;
    public GlobalApiHandler(CoinGeckoGeneralServicesApi generalService) {
        this.generalService = generalService;
    }
    public Mono<ServerResponse> getGlobalDataFromGeckoApi(ServerRequest serverRequest){
        log.info("Fetching global data from CoinGecko API");

        return generalService.getGlobalData()
                .flatMap(data -> ServerResponse.ok().bodyValue(data))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CoinGeckoDataNotFoudException("Global data not found"))))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue("Error fetching global data: " + e.getMessage()));
    }
    public Mono<ServerResponse> getDecentralizedFinanceDefi(ServerRequest sRequest) {
        log.info("Fetching decentralized finance data from CoinGecko API");

        return generalService.getDecentralizedFinance()
                .flatMap(defi -> ServerResponse.ok().bodyValue(defi))
                .switchIfEmpty(Mono.defer(() -> ServerResponse.notFound().build()))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue("Error fetching decentralized finance data: " + e.getMessage()));
    }

}