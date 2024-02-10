package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.exception.ServiceException;
import ar.com.api.general.model.Search;
import ar.com.api.general.model.Trending;
import ar.com.api.general.model.TrendingCoin;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CoinGeckoSearchAPIServiceTest {

    private CoinGeckoSearchAPIService coinGeckoSearchAPIService;
    private WebClient webClientMock;
    private WebClient.ResponseSpec responseSpecMock;
    private ExternalServerConfig externalServerConfig;

    @BeforeEach
    void setUp() {
        webClientMock = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriMock = Mockito
                .mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersMock = Mockito
                .mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        externalServerConfig = Mockito.mock(ExternalServerConfig.class);
        when(externalServerConfig.getSearch())
                .thenReturn("mockServiceSearchApiUrl");
        when(externalServerConfig.getSearchTrending())
                .thenReturn("mockTrendingUrl");

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(anyString())).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);

        coinGeckoSearchAPIService = new CoinGeckoSearchAPIService(webClientMock, externalServerConfig);
    }

    @Test
    void getSearchFromGeckoApi_ShouldReturnSearchSuccessfully() {

        Search expectedSearch = Instancio.create(Search.class);
        when(responseSpecMock.bodyToMono(Search.class)).thenReturn(Mono.just(expectedSearch));

        SearchDTO searchDTOMock = Instancio.create(SearchDTO.class);
        Search actualSearch = coinGeckoSearchAPIService.getSearchFromGeckoApi(searchDTOMock).block();

        assertEquals(expectedSearch, actualSearch);
    }

    @Test
    void getSearchFromGeckoApi_ShouldHandleBadRequest400Error() {

        WebClientResponseException mockException = WebClientResponseException
                .create(400, "bad Request", null, null, null);
        when(responseSpecMock.bodyToMono(Search.class)).thenReturn(Mono.error(mockException));

        SearchDTO searchDTOMock = Instancio.create(SearchDTO.class);
        Mono<Search> resultSearchException = coinGeckoSearchAPIService
                .getSearchFromGeckoApi(searchDTOMock);

        StepVerifier.create(resultSearchException)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException
                                && throwable.getCause() instanceof WebClientResponseException
                                && ((WebClientResponseException) throwable.getCause())
                                    .getStatusCode().is4xxClientError())
                .verify();
    }

    @Test
    void getSearchFromGeckoApi_ShouldHandleInternalServer500Error() {
        WebClientResponseException mockException = WebClientResponseException
                .create(500, "Internal Server Error", null, null, null);
        when(responseSpecMock.bodyToMono(Search.class)).thenReturn(Mono.error(mockException));

        SearchDTO searchDTOMock = Instancio.create(SearchDTO.class);
        Mono<Search> resultSearchException = coinGeckoSearchAPIService
                .getSearchFromGeckoApi(searchDTOMock);
        StepVerifier.create(resultSearchException)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException
                                && throwable.getCause() instanceof WebClientResponseException
                                && ((WebClientResponseException) throwable.getCause())
                                    .getStatusCode().is5xxServerError() )
                .verify();
    }

    @Test
    public void getSearchTrendingFromGeckoApi_ShouldReturnTrendingSuccessfully () {
        Trending trendingExpected = Instancio.create(Trending.class);
        when(responseSpecMock.bodyToMono(Trending.class)).thenReturn(Mono.just(trendingExpected));

        Mono<Trending> resultSearchActual = coinGeckoSearchAPIService
                .getSearchTrendingFromGeckoApi();
        List<TrendingCoin> trendingCoinsList = resultSearchActual.block().getCoins();

        StepVerifier.create(resultSearchActual)
                .expectNext(trendingExpected)
                .verifyComplete();
        assertTrue(trendingCoinsList.size() > 0);
    }

    @Test
    public void getSearchTrendingFromGeckoApi_ShouldHandleBadRequest400Error() {
        WebClientResponseException mockException = WebClientResponseException.create(
                400, "Bad Request", null, null, null
        );
        when(responseSpecMock.bodyToMono(Trending.class)).thenReturn(Mono.error(mockException));

        Mono<Trending> resultException = coinGeckoSearchAPIService.getSearchTrendingFromGeckoApi();

        StepVerifier.create(resultException)
                .expectErrorMatches(throwable ->
                    throwable instanceof ServiceException &&
                            throwable.getCause() instanceof WebClientResponseException &&
                            ((WebClientResponseException)throwable.getCause()).getStatusCode().is4xxClientError()
                ).verify();
    }

    @Test
    public void getSearchTrendingFromGeckoApi_ShouldHandleInternalServer500Error() {
        WebClientResponseException mock5xxException = WebClientResponseException.create(
                500, "Internal Server Error", null, null, null, null
        );
        when(responseSpecMock.bodyToMono(Trending.class)).thenReturn(Mono.error(mock5xxException));

        Mono<Trending> result5xxException = coinGeckoSearchAPIService.getSearchTrendingFromGeckoApi();

        StepVerifier.create(result5xxException)
                .expectErrorMatches(throwable ->
                        throwable instanceof ServiceException &&
                                throwable.getCause() instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable.getCause()).getStatusCode().is5xxServerError()
                ).verify();
    }

}