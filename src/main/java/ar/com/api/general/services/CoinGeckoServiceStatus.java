package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.configuration.HttpServiceCall;
import ar.com.api.general.model.Ping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus {

    private final ExternalServerConfig externalServerConfig;
    private final HttpServiceCall httpServiceCall;

    public CoinGeckoServiceStatus(HttpServiceCall serviceCall, ExternalServerConfig externalServerConfig) {
        this.httpServiceCall = serviceCall;
        this.externalServerConfig = externalServerConfig;
    }

    public Mono<Ping> getStatusCoinGeckoService() {
        log.info("Calling endpoint on GeckoApi {}", externalServerConfig.getPing());

        return httpServiceCall.getMonoObject(externalServerConfig.getPing(), Ping.class);
    }

}
