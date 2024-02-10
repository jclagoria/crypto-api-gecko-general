package ar.com.api.general.handler;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.model.Search;
import ar.com.api.general.router.SearchRouterConfig;
import ar.com.api.general.services.CoinGeckoSearchAPIService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SearchApiHandlerRouterTest {
    private WebTestClient webTestClient;
    private CoinGeckoSearchAPIService searchAPIServiceMock;
    private ApiServiceConfig apiServiceConfigMock;
    private Validator validatorMock;

    @BeforeEach
    void setUp() {
        searchAPIServiceMock = Mockito.mock(CoinGeckoSearchAPIService.class);
        apiServiceConfigMock = Mockito.mock(ApiServiceConfig.class);
        validatorMock = Mockito.mock(Validator.class);
        SearchApiHandler handler = new SearchApiHandler(searchAPIServiceMock, validatorMock);

        when(apiServiceConfigMock.getBaseURL()).thenReturn("/api");
        when(apiServiceConfigMock.getSearch()).thenReturn("/search");

        SearchRouterConfig searchRouterConfig = new SearchRouterConfig(apiServiceConfigMock);

        webTestClient = WebTestClient
                .bindToRouterFunction(searchRouterConfig.routeSearch(handler)).build();
    }

    @Test
    void getListOfCoinsWithSearchAPI_Success() {
        Search searchMock = Instancio.create(Search.class);
        when(searchAPIServiceMock.getSearchFromGeckoApi(any()))
                .thenReturn(Mono.just(searchMock));

        webTestClient.get()
                .uri(
                        apiServiceConfigMock.getBaseURL() +
                                apiServiceConfigMock.getSearch() + "?queryParam=test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Search.class).isEqualTo(searchMock);
    }

}
