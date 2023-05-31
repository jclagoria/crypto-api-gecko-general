package ar.com.api.general.handler;

import ar.com.api.general.model.Global;
import ar.com.api.general.services.CoinGeckoGeneralServicesApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class GlobalApiHandler {

    private CoinGeckoGeneralServicesApi generalService;

    public Mono<ServerResponse> getGlobalDataFromGeckoApi(ServerRequest serverRequest){

        log.info("In GlobalApiHandler.getGlobalDataFromGeckoApi");

        return ServerResponse
                .ok()
                .body(
                        generalService.getGlobalData(),
                        Global.class
                );
    }

}
