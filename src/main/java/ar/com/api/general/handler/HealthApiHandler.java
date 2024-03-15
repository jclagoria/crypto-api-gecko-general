package ar.com.api.general.handler;

import ar.com.api.general.services.CoinGeckoServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class HealthApiHandler {
    private CoinGeckoServiceStatus serviceStatus;

    public HealthApiHandler(CoinGeckoServiceStatus healthService) {
        this.serviceStatus = healthService;
    }

    public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {
        log.info("Fetching CoinGecko service status");

        return serviceStatus.getStatusCoinGeckoService()
                .flatMap(ping -> ServerResponse.ok().bodyValue(ping))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> {
                    log.error("Error fetching CoinGecko service status", e);
                    return ServerResponse.status(500).bodyValue("Error fetching service status");
                });
    }


}
