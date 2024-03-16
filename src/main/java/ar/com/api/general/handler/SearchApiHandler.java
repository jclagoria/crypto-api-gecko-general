package ar.com.api.general.handler;

import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.model.Search;
import ar.com.api.general.services.CoinGeckoSearchAPIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SearchApiHandler {
    private final CoinGeckoSearchAPIService searchAPIService;
    private Validator validator;

    public SearchApiHandler(CoinGeckoSearchAPIService sApiService, Validator validator) {
        this.searchAPIService = sApiService;
        this.validator = validator;
    }

    public Mono<ServerResponse> getListOfCoinsWithSearchAPI(ServerRequest sRequest) {

        log.info("In SearchApiHandler.getListOfCoinsWithSearchAPI");

        return Mono.just(sRequest)
                .map(req -> SearchDTO
                        .builder()
                        .queryParam(
                                sRequest.queryParam("queryParam").get())
                        .build())
                .flatMap(this::validateSearchDTO)
                .flatMap(filterDTO -> ServerResponse.ok()
                        .body(searchAPIService
                                .getSearchFromGeckoApi(filterDTO), Search.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()));

    }

    public Mono<ServerResponse> getTrendingOfCoinsAPI(ServerRequest serverRequest) {
        log.info("Fetching trending coins");

        return searchAPIService.getSearchTrendingFromGeckoApi()
                .flatMap(trending -> ServerResponse.ok().bodyValue(trending))
                .switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));

    }

    private Mono<SearchDTO> validateSearchDTO(SearchDTO filterDTO) {
        Errors errors = new BeanPropertyBindingResult(filterDTO, SearchDTO.class.getName());
        validator.validate(filterDTO, errors);

        if (errors.hasErrors()) {
            String errorMsg = errors.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce("", (partialString, element) -> partialString + element + "; ");
            return Mono.error(new IllegalArgumentException("Validation error: " + errorMsg));
        }

        return Mono.just(filterDTO);
    }

}