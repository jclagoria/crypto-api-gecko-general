package ar.com.api.general.handler.utils;

import ar.com.api.general.dto.SearchDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class MapperHandler {

    public static Mono<SearchDTO> createSearchDTOFromRequest(ServerRequest sRequest) {
        return Mono.just(SearchDTO
                .builder()
                .queryParam(
                        sRequest.queryParam("queryParam").get())
                .build());
    }

}
