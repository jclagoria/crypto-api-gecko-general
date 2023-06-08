package ar.com.api.general.services;

import ar.com.api.general.dto.SimplePriceFilterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
public class CoinGeckoSimpleApiService {

    @Value("${api.simplePrice}")
    private String URL_COIN_GECKO_SIMPLE_API_SERVICE;
    private WebClient webClient;
    public CoinGeckoSimpleApiService(WebClient wClient) {
        this.webClient = wClient;
    }

    public Mono<Map> getSimplePriceApiService(SimplePriceFilterDTO filterDTO) {

        log.info("in getSimplePriceApiService - Calling Gecko Api Service");

        return webClient
                .get()
                .uri(URL_COIN_GECKO_SIMPLE_API_SERVICE + filterDTO.getUrlFilterString())
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(throwable -> log.error("The service is unavailable!", throwable));
    }


}
