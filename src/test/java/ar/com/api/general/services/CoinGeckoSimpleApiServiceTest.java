package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.dto.SimplePriceFilterDTO;
import ar.com.api.general.dto.TokenPriceByIdDTO;
import ar.com.api.general.exception.ServiceException;
import ar.com.api.general.utils.WebClientMockUtils;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoinGeckoSimpleApiServiceTest {
    private CoinGeckoSimpleApiService coinGeckoSimpleApiServiceMock;
    private WebClient webClientMock;
    private WebClient.ResponseSpec responseSpecMock;
    private ExternalServerConfig externalServerConfig;

    @BeforeEach
    void setUp() {
        responseSpecMock = WebClientMockUtils.mockResponseSpec();
        webClientMock = WebClientMockUtils.mockWebClient(responseSpecMock);
        externalServerConfig = mock(ExternalServerConfig.class);

        when(externalServerConfig.getSimplePrice()).thenReturn("simplePriceUrl");
        when(externalServerConfig.getSimpleTokePriceById()).thenReturn("simpleTokePriceByIdUrl");

        coinGeckoSimpleApiServiceMock = new CoinGeckoSimpleApiService(webClientMock, externalServerConfig);
    }

    @Test
    void getSimplePriceApiService_ShouldReturnMapSuccessfully() {

        Map<String, Double> expectedMap = Map.of("bitcoin", 40000.0);
        when(responseSpecMock.bodyToMono(Map.class)).thenReturn(Mono.just(expectedMap));

        SimplePriceFilterDTO filterDTOMock = Instancio.create(SimplePriceFilterDTO.class);

        Mono<Map> actualMap = coinGeckoSimpleApiServiceMock.getSimplePriceApiService(filterDTOMock);

        StepVerifier.create(actualMap)
                .assertNext(actual -> {
                    Assertions.assertThat(actual)
                            .isNotEmpty()
                            .containsExactlyInAnyOrderEntriesOf(expectedMap);
                })
                .verifyComplete();
    }

    @Test
    void getSimplePriceApiService_ShouldHandleBadRequest4xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                400, "Bad Request", null, null, null
        );
        when(responseSpecMock.bodyToMono(Map.class)).thenReturn(Mono.error(mockException));

        SimplePriceFilterDTO filterDTOMock = Instancio.create(SimplePriceFilterDTO.class);

        Mono<Map> error4xxExpected = coinGeckoSimpleApiServiceMock.getSimplePriceApiService(filterDTOMock);

        StepVerifier.create(error4xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause())
                                        .getStatusCode().is4xxClientError())
                .verify();
    }

    @Test
    void getSimplePriceApiService_ShouldInternalServer5xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                500, "Internal Server Error", null, null, null
        );
        when(responseSpecMock.bodyToMono(Map.class)).thenReturn(Mono.error(mockException));

        SimplePriceFilterDTO filterDTOMock = Instancio.create(SimplePriceFilterDTO.class);

        Mono<Map> error5xxExpected = coinGeckoSimpleApiServiceMock.getSimplePriceApiService(filterDTOMock);

        StepVerifier.create(error5xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause())
                                        .getStatusCode().is5xxServerError())
                .verify();
    }

    @Test
    void getSimplePriceTokenById_ShouldReturnMapSuccessfully() {

        Map<String, Double> expectedMap = Map.of("ethereum", 1200.0);
        when(responseSpecMock.bodyToMono(Map.class)).thenReturn(Mono.just(expectedMap));

        TokenPriceByIdDTO tokenPriceByIdDTO = Instancio.create(TokenPriceByIdDTO.class);

        Mono<Map> actualSimplePriceTokenByIdMap = coinGeckoSimpleApiServiceMock
                .getSimplePriceTokenById(tokenPriceByIdDTO);

        StepVerifier.create(actualSimplePriceTokenByIdMap)
                .assertNext(actual -> {
                    Assertions.assertThat(actual)
                            .isNotEmpty()
                            .containsExactlyInAnyOrderEntriesOf(expectedMap);
                })
                .verifyComplete();
    }

    @Test
    void getSimplePriceTokenById_ShouldHandleBadRequest4xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                400, "Bad Request", null, null, null
        );
        when(responseSpecMock.bodyToMono(Map.class)).thenReturn(Mono.error(mockException));

        TokenPriceByIdDTO tokenPriceByIdDTO = Instancio.create(TokenPriceByIdDTO.class);

        Mono<Map> error4xxExpected = coinGeckoSimpleApiServiceMock
                .getSimplePriceTokenById(tokenPriceByIdDTO);

        StepVerifier.create(error4xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause())
                                        .getStatusCode().is4xxClientError())
                .verify();
    }

    @Test
    void getSimplePriceTokenById_ShouldHandleBadRequest5xxError() {
        WebClientResponseException mockException = WebClientResponseException.create(
                500, "Internal Server Error", null, null, null
        );
        when(responseSpecMock.bodyToMono(Map.class)).thenReturn(Mono.error(mockException));

        TokenPriceByIdDTO tokenPriceByIdDTO = Instancio.create(TokenPriceByIdDTO.class);

        Mono<Map> error5xxExpected = coinGeckoSimpleApiServiceMock
                .getSimplePriceTokenById(tokenPriceByIdDTO);

        StepVerifier.create(error5xxExpected)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause())
                                        .getStatusCode().is5xxServerError())
                .verify();
    }

}