package ar.com.api.general.configuration;

import ar.com.api.general.enums.ErrorTypeEnum;
import ar.com.api.general.exception.ApiServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class HttpServiceCall {

    private final WebClient webClient;

    public HttpServiceCall(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> Mono<T> getMonoObject(String urlEndPoint, Class<T> responseType) {
        return configurableResponseSpec(urlEndPoint)
                .bodyToMono(responseType)
                .doOnSubscribe(subscription -> log.info("Fetch data from CoinGecko service {}", urlEndPoint))
                .onErrorResume(this::handleError);
    }

    public <T> Flux<T> getFluxObject(String urlEndPoint, Class<T> responseType) {
        return configurableResponseSpec(urlEndPoint)
                .bodyToFlux(responseType)
                .doOnSubscribe(subscription -> log.info("Fetch data from ConGecko service {}", urlEndPoint))
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> handleError(Throwable throwable) {
        return Mono.error(throwable instanceof ApiServerErrorException ?
                throwable : new Exception("General Error", throwable));
    }

    private Mono<ApiServerErrorException> handleResponseError(Map<String, Object> errorMessage,
                                                              HttpStatus status,
                                                              ErrorTypeEnum typeEnum) {
        String errorBody = (String) errorMessage.getOrDefault("error", "Unknown error");
        return Mono.error(new ApiServerErrorException(status.getReasonPhrase(), errorBody, typeEnum, status));
    }

    private WebClient.ResponseSpec configurableResponseSpec(String urlEndPoint) {
        return webClient.get()
                .uri(urlEndPoint)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse
                                .bodyToMono(Map.class)
                                .flatMap(errorMessage ->
                                        handleResponseError(errorMessage,
                                                (HttpStatus) clientResponse.statusCode(),
                                                ErrorTypeEnum.GECKO_CLIENT_ERROR))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse
                                .bodyToMono(Map.class)
                                .flatMap(errorMessage ->
                                        handleResponseError(errorMessage,
                                                (HttpStatus) clientResponse.statusCode(),
                                                ErrorTypeEnum.GECKO_SERVER_ERROR))
                );
    }

}
