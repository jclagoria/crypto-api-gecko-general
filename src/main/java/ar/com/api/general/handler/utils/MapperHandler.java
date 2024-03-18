package ar.com.api.general.handler.utils;

import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.dto.SimplePriceFilterDTO;
import ar.com.api.general.dto.TokenPriceByIdDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class MapperHandler {

    public static Mono<SearchDTO> createSearchDTOFromRequest(ServerRequest sRequest) {
        return Mono.just(SearchDTO
                .builder()
                .queryParam(
                        sRequest.queryParam("queryParam").get())
                .build());
    }

    public static Mono<SimplePriceFilterDTO> createSimplePriceFilterDTOFromRequest(ServerRequest sRequest) {
        return Mono.just(SimplePriceFilterDTO.builder()
                .ids(sRequest.queryParam("ids").get())
                .vsCurrencies(sRequest.queryParam("vsCurrencies").get())
                .includeMarketCap(sRequest.queryParam("includeMarketCap"))
                .include24hrVol(sRequest.queryParam("include24hrVol"))
                .include24hrChange(sRequest.queryParam("include24hrChange"))
                .includeLastUpdatedAt(sRequest.queryParam("includeLastUpdatedAt"))
                .precision(sRequest.queryParam("precision"))
                .build());
    }

    public static Mono<TokenPriceByIdDTO> createTokenPriceByIdDTOFromRequest(ServerRequest sRequest) {
        return Mono.just(TokenPriceByIdDTO.builder()
                .ids(sRequest.pathVariable("ids"))
                .contractAddresses(String.valueOf(sRequest.queryParam("contractAddress")))
                .vsCurrencies(String.valueOf(sRequest.queryParam("vsCurrencies")))
                .includeMarketCap(sRequest.queryParam("includeMarketCap"))
                .include24hrVol(sRequest.queryParam("include24hrVol"))
                .include24hrChange(sRequest.queryParam("include24hrChange"))
                .includeLastUpdatedAt(sRequest.queryParam("includeLastUpdatedAt"))
                .precision(sRequest.queryParam("precision"))
                .build());
    }

}
