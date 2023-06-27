package ar.com.api.general.handler;

import ar.com.api.general.dto.EmpyDataDTO;
import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.model.Search;
import ar.com.api.general.model.Trending;
import ar.com.api.general.services.CoinGeckoSearchAPIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class SearchApiHandler {

    private CoinGeckoSearchAPIService searchAPIService;
    private Validator validator;

    public Mono<ServerResponse> getListOfCoinsWithSearchAPI(ServerRequest sRequest) {

        log.info("In SearchApiHandler.getListOfCoinsWithSearchAPI");

        SearchDTO filterDTO = SearchDTO
                .builder()
                .queryParam(sRequest.queryParam("query").get())
                .build();

        Errors errors = new BeanPropertyBindingResult(filterDTO, SearchDTO.class.getName());
        this.validator.validate(filterDTO, errors);

        if(errors.getAllErrors().isEmpty()){
            return ServerResponse
                    .ok()
                    .body(
                            searchAPIService.getSearchFromGeckoApi(filterDTO),
                            Search.class
                    ).switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));
        } else {
            return onValidationErrors(errors);

        }

    }

    public Mono<ServerResponse> getTrendingOfCoinsAPI(ServerRequest serverRequest) {

        log.info("In SearchApiHandler.getTrendingOfCoinsAPI");

        return ServerResponse
                .ok()
                .body(
                        searchAPIService.getSearchTrendingFromGeckoApi(),
                        Trending.class
                ).switchIfEmpty(Mono.defer(() -> ServerResponse.noContent().build()));

    }

    /**
     * Cambiar a un erro en particular, donde erros sea una lista de strings
     * @param errors
     * @return
     */
    private Mono<ServerResponse> onValidationErrors(Errors errors) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                errors.getAllErrors().toString()
        );
    }

}
