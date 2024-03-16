package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.configuration.HttpServiceCall;
import ar.com.api.general.model.DecentralizedFinance;
import ar.com.api.general.model.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoGeneralServicesApi {
    private final ExternalServerConfig externalServerConfig;
    private final HttpServiceCall httpServiceCall;

    public CoinGeckoGeneralServicesApi(HttpServiceCall serviceCall, ExternalServerConfig externalServerConfig) {
        this.httpServiceCall = serviceCall;
        this.externalServerConfig = externalServerConfig;
    }

    public Mono<Global> getGlobalData() {

        log.info("in getGlobalData - Calling Gecko Api Service -> " + externalServerConfig.getGlobal());

        return null;
    }

    public Mono<DecentralizedFinance> getDecentralizedFinance() {
        log.info("getGlobalData - Calling DecentralizedFinance -> " + externalServerConfig.getDecentralized());

        return null;
    }

}
