package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.exception.ServiceException;
import ar.com.api.general.model.Ping;
import ar.com.api.general.utils.WebClientMockUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoinGeckoServiceStatusTest {

    private CoinGeckoServiceStatus coinGeckoServiceStatusMock;
    private WebClient webClientMock;
    private WebClient.ResponseSpec responseSpecMock;
    private ExternalServerConfig externalServerConfigMock;

    @BeforeEach
    void setUp() {

        responseSpecMock = WebClientMockUtils.mockResponseSpec();
        webClientMock = WebClientMockUtils.mockWebClient(responseSpecMock);
        externalServerConfigMock = mock(ExternalServerConfig.class);

        when(externalServerConfigMock.getPing()).thenReturn("ping");

        coinGeckoServiceStatusMock = new CoinGeckoServiceStatus(webClientMock, externalServerConfigMock);
    }

    @Test
    void getStatusCoinGeckoService_ShouldReturnPingSuccessfully() {
        Ping expectedPing = Instancio.create(Ping.class);
        when(responseSpecMock.bodyToMono(Ping.class)).thenReturn(Mono.just(expectedPing));

        Mono<Ping> actualPing = coinGeckoServiceStatusMock.getStatusCoinGeckoService();

        StepVerifier.create(actualPing)
                .expectNext(expectedPing)
                .verifyComplete();
    }

    @Test
    void getStatusCoinGeckoService_ShouldHandleBadRequest4xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                400, "Bad Request", null, null, null
        );
        when(responseSpecMock.bodyToMono(Ping.class)).thenReturn(Mono.error(mockException));

        Mono<Ping> error4xxExpected = coinGeckoServiceStatusMock.getStatusCoinGeckoService();

        StepVerifier.create(error4xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException)throwable.getCause())
                                        .getStatusCode().is4xxClientError())
                .verify();
    }

    @Test
    void getStatusCoinGeckoService_ShouldHandleServerInternal5xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                500, "Internal Server Error", null, null, null
        );
        when(responseSpecMock.bodyToMono(Ping.class)).thenReturn(Mono.error(mockException));

        Mono<Ping> error5xxExpected = coinGeckoServiceStatusMock.getStatusCoinGeckoService();

        StepVerifier.create(error5xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException)throwable.getCause())
                                        .getStatusCode().is5xxServerError())
                .verify();
    }


}