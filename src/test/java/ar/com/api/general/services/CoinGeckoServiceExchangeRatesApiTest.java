package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.exception.ServiceException;
import ar.com.api.general.model.ExchangeRate;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CoinGeckoServiceExchangeRatesApiTest {

    private CoinGeckoServiceExchangeRatesApi coinGeckoServiceExchangeRatesApi;
    private WebClient webClientMock;
    private WebClient.ResponseSpec responseSpecMock;
    private ExternalServerConfig externalServerConfigMock;

    @BeforeEach
    void SetUP() {
        webClientMock = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = Mockito
                .mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = Mockito
                .mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        externalServerConfigMock = Mockito.mock(ExternalServerConfig.class);
        when(externalServerConfigMock.getExchangeRates()).thenReturn("exchangeRatesUrl");

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);

        coinGeckoServiceExchangeRatesApi = new CoinGeckoServiceExchangeRatesApi(webClientMock,
                externalServerConfigMock);
    }

    @Test
    void getExchangeRatesFromGeckoApi_ShouldReturnExchangeRateSuccessFully() {
        ExchangeRate expectedExchangeRate = Instancio.create(ExchangeRate.class);
        when(responseSpecMock.bodyToMono(ExchangeRate.class)).thenReturn(Mono.just(expectedExchangeRate));

        Mono<ExchangeRate> actualExchangeRate = coinGeckoServiceExchangeRatesApi.getExchangeRatesFromGeckoApi();

        StepVerifier.create(actualExchangeRate)
                .expectNext(expectedExchangeRate)
                .verifyComplete();
    }

    @Test
    void getExchangeRatesFromGeckoApi_ShouldHandleBadRequest4xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                400, "Bad Request", null, null, null
        );
        when(responseSpecMock.bodyToMono(ExchangeRate.class)).thenReturn(Mono.error(mockException));

        Mono<ExchangeRate> error4xxExpected = coinGeckoServiceExchangeRatesApi.getExchangeRatesFromGeckoApi();

        StepVerifier.create(error4xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause()).getStatusCode().is4xxClientError())
                .verify();
    }

    @Test
    void getExchangeRatesFromGeckoApi_ShouldHandleInternalServer5xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                500, "Internal Server Exception", null, null, null
        );
        when(responseSpecMock.bodyToMono(ExchangeRate.class)).thenReturn(Mono.error(mockException));

        Mono<ExchangeRate> error5xxExpected = coinGeckoServiceExchangeRatesApi.getExchangeRatesFromGeckoApi();

        StepVerifier.create(error5xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause()).getStatusCode().is5xxServerError())
                .verify();
    }
}