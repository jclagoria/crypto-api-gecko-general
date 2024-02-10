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
@Slf4j
public class SimpleApiHandler {
    private CoinGeckoSimpleApiService coinGeckoSimpleApiService;
    public SimpleApiHandler(CoinGeckoSimpleApiService simpleApiService) {
        this.coinGeckoSimpleApiService = simpleApiService;
    }
    public Mono<ServerResponse> getSimplePriceFromCoinGeckoApi(ServerRequest serverRequest){
        log.info("Fetching simple price from CoinGecko API");

        return Mono.just(serverRequest)
                .flatMap(req -> {
                    SimplePriceFilterDTO simplePriceFilterDTO = createSimplePriceFilterDTO(serverRequest);
                    return ServerResponse.ok()
                            .body(coinGeckoSimpleApiService
                                    .getSimplePriceApiService(simplePriceFilterDTO), Map.class);
                }).switchIfEmpty(ServerResponse.badRequest().build());
    }
    public Mono<ServerResponse> getSimplePriceTokenByIDFromCoinGeckoApi(ServerRequest serverRequest){
        log.info("Fetching simple price token by ID from CoinGecko API");

        return Mono.just(serverRequest)
                .flatMap(req -> {
                    TokenPriceByIdDTO tokenPriceByIdDTO = createTokenPriceByIdDTO(serverRequest);
                    return ServerResponse.ok().body(coinGeckoSimpleApiService.getSimplePriceTokenById(tokenPriceByIdDTO), Map.class);
                })
                .switchIfEmpty(ServerResponse.badRequest().build());
    }
    private SimplePriceFilterDTO createSimplePriceFilterDTO(ServerRequest sRequest) {
        return SimplePriceFilterDTO.builder()
                .ids(sRequest.queryParam("ids")
                        .orElseThrow(() -> new IllegalArgumentException("IDs parameter is required")))
                .vsCurrencies(sRequest.queryParam("vsCurrencies")
                        .orElseThrow(() -> new IllegalArgumentException("vsCurrencies parameter is required")))
                .includeMarketCap(sRequest.queryParam("includeMarketCap"))
                .include24hrVol(sRequest.queryParam("include24hrVol"))
                .include24hrChange(sRequest.queryParam("include24hrChange"))
                .includeLastUpdatedAt(sRequest.queryParam("includeLastUpdatedAt"))
                .precision(sRequest.queryParam("precision"))
                .build();
    }
    private TokenPriceByIdDTO createTokenPriceByIdDTO(ServerRequest sRequest) {
        return TokenPriceByIdDTO.builder()
                .ids(sRequest.pathVariable("ids"))
                .contractAddresses(sRequest.queryParam("contractAddress")
                        .orElseThrow(() -> new IllegalArgumentException("Contract address parameter is required")))
                .vsCurrencies(sRequest.queryParam("vsCurrencies")
                        .orElseThrow(() -> new IllegalArgumentException("vsCurrencies parameter is required")))
                .includeMarketCap(sRequest.queryParam("includeMarketCap"))
                .include24hrVol(sRequest.queryParam("include24hrVol"))
                .include24hrChange(sRequest.queryParam("include24hrChange"))
                .includeLastUpdatedAt(sRequest.queryParam("includeLastUpdatedAt"))
                .precision(sRequest.queryParam("precision"))
                .build();
    }

}