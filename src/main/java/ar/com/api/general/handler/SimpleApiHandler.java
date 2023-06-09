package ar.com.api.general.handler;

import ar.com.api.general.dto.SimplePriceFilterDTO;
import ar.com.api.general.dto.TokenPriceByIdDTO;
import ar.com.api.general.services.CoinGeckoSimpleApiService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class SimpleApiHandler {

    private CoinGeckoSimpleApiService coinGeckoSimpleApiService;

    public Mono<ServerResponse> getSimplePriceFromCoinGeckoApi(ServerRequest serverRequest){

        SimplePriceFilterDTO filterDTO = SimplePriceFilterDTO
                .builder()
                .ids(serverRequest.queryParam("ids").get())
                .vsCurrencies(serverRequest.queryParam("vsCurrencies").get())
                .include24hrChange(serverRequest.queryParam("includeMarketCap"))
                .include24hrVol(serverRequest.queryParam("include24hrVol"))
                .includeLastUpdatedAt(serverRequest.queryParam("include24hrChange"))
                .includeMarketCap(serverRequest.queryParam("includeLastUpdatedAt"))
                .precision(serverRequest.queryParam("precision"))
                .build();

        return ServerResponse
                .ok()
                .body(
                        coinGeckoSimpleApiService.getSimplePriceApiService(filterDTO),
                        Map.class
                );
    }

    public Mono<ServerResponse> getSimplePriceTokenByIDFromCoinGeckoApi(ServerRequest serverRequest){

        TokenPriceByIdDTO filterDTO = TokenPriceByIdDTO
                .builder()
                .ids(serverRequest.pathVariable("ids"))
                .contractAddresses(serverRequest.queryParam("contractAddress").get())
                .vsCurrencies(serverRequest.queryParam("vsCurrencies").get())
                .include24hrChange(serverRequest.queryParam("include24hrChange"))
                .include24hrVol(serverRequest.queryParam("include24hrVol"))
                .includeLastUpdatedAt(serverRequest.queryParam("includeLastUpdatedAt"))
                .includeMarketCap(serverRequest.queryParam("includeMarketCap"))
                .precision(serverRequest.queryParam("precision"))
                .build();

        return ServerResponse
                .ok()
                .body(
                        coinGeckoSimpleApiService.getSimplePriceTokenById(filterDTO),
                        Map.class
                );
    }

}
