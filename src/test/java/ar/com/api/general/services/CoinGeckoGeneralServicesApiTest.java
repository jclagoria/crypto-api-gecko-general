package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.exception.ServiceException;
import ar.com.api.general.model.DecentralizedFinance;
import ar.com.api.general.model.Global;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CoinGeckoGeneralServicesApiTest {

    private CoinGeckoGeneralServicesApi coinGeckoGeneralServicesApi;
    private WebClient webClientMock;
    private WebClient.ResponseSpec responseSpecMock;
    private ExternalServerConfig externalServerConfigMock;

    @BeforeEach
    void setUp() {
        webClientMock = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        externalServerConfigMock = Mockito.mock(ExternalServerConfig.class);
        when(externalServerConfigMock.getGlobal()).thenReturn("mockGlobalUrl");
        when(externalServerConfigMock.getDecentralized()).thenReturn("mockDecentralizedUrl");

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(anyString())).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);

        coinGeckoGeneralServicesApi = new CoinGeckoGeneralServicesApi(webClientMock,
                externalServerConfigMock);
    }

    @Test
    void getGlobalData_ShouldReturnGlobalDataSuccessfully() {
        Global expectedGlobal = Instancio.create(Global.class);
        when(responseSpecMock.bodyToMono(Global.class)).thenReturn(Mono.just(expectedGlobal));

        Global actualGlobal = coinGeckoGeneralServicesApi.getGlobalData().block();

        assertEquals(expectedGlobal, actualGlobal);
    }

    @Test
    void getGlobalData_ShouldHandleBadRequest400Error() {
        WebClientResponseException mockException = WebClientResponseException
                .create(400, "Bad Request", null, null, null);
        when(responseSpecMock.bodyToMono(Global.class)).thenReturn(Mono.error(mockException));

        Mono<Global> result = coinGeckoGeneralServicesApi.getGlobalData();

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause()).getStatusCode().is4xxClientError()
                )
                .verify();
    }

    @Test
    void getGlobalData_ShouldHandleInternalServer500Error() {
        WebClientResponseException mockException = WebClientResponseException
                .create(500, "Internal Server Error", null, null, null);
        when(responseSpecMock.bodyToMono(Global.class)).thenReturn(Mono.error(mockException));

        Mono<Global> result = coinGeckoGeneralServicesApi.getGlobalData();

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause()).getStatusCode().is5xxServerError()
                )
                .verify();
    }

    @Test
    void getDecentralizedFinance_ShouldReturnDecentralizedFinanceSuccessfully() {
        DecentralizedFinance expectedDecentralizedFinance = Instancio.create(DecentralizedFinance.class);
        when(responseSpecMock.bodyToMono(DecentralizedFinance.class)).thenReturn(Mono.just(expectedDecentralizedFinance));

        DecentralizedFinance actualGlobal = coinGeckoGeneralServicesApi.getDecentralizedFinance().block();

        assertEquals(expectedDecentralizedFinance, actualGlobal);
    }

    @Test
    void getDecentralizedFinance_ShouldHandleBadRequest400Error() {
        WebClientResponseException mockException = WebClientResponseException.create(
                400, "Bad Request", null, null, null);
        when(responseSpecMock.bodyToMono(DecentralizedFinance.class)).thenReturn(Mono.error(mockException));

        Mono<DecentralizedFinance> result = coinGeckoGeneralServicesApi.getDecentralizedFinance();

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause()).getStatusCode().is4xxClientError()
                )
                .verify();
    }

    @Test
    void getDecentralizedFinance_ShouldHandleBadRequest500Error() {
        WebClientResponseException mockException = WebClientResponseException
                .create(500, "Internal Server Error", null, null, null);
        when(responseSpecMock.bodyToMono(DecentralizedFinance.class)).thenReturn(Mono.error(mockException));

        Mono<DecentralizedFinance> result = coinGeckoGeneralServicesApi.getDecentralizedFinance();

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause()).getStatusCode().is5xxServerError()
                ).verify();
    }

}