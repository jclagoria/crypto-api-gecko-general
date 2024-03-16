package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.configuration.HttpServiceCall;
import ar.com.api.general.dto.SimplePriceFilterDTO;
import ar.com.api.general.dto.TokenPriceByIdDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
public class CoinGeckoSimpleApiService {
    private final ExternalServerConfig externalServerConfig;
    private final HttpServiceCall httpServiceCall;

    public CoinGeckoSimpleApiService(HttpServiceCall serviceCall, ExternalServerConfig externalServerConfig) {
        this.httpServiceCall = serviceCall;
        this.externalServerConfig = externalServerConfig;
    }

    public Mono<Map> getSimplePriceApiService(SimplePriceFilterDTO filterDTO) {

        log.info("in getSimplePriceApiService - Calling Gecko Api Service -> "
                + externalServerConfig.getSimplePrice()
                + filterDTO.getUrlFilterString());

        return null;
    }

    public Mono<Map> getSimplePriceTokenById(TokenPriceByIdDTO filterDTO) {

        log.info("in getSimplePriceTokenById - Calling Gecko Api Service -> " +
                String.format(externalServerConfig.getSimpleTokePriceById(),
                        filterDTO.getIds())
                + filterDTO.getUrlFilterString());

        return null;
    }

}
