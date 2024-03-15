package ar.com.api.general.handler;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.router.SimpleRouteConfig;
import ar.com.api.general.services.CoinGeckoSimpleApiService;
import org.instancio.Instancio;
import org.instancio.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleApiHandlerRouterConfigTest {

    private WebTestClient webTestClient;
    private CoinGeckoSimpleApiService coinGeckoSimpleApiServiceMock;
    private ApiServiceConfig apiServiceConfigMock;

    private SimpleApiHandler handler;

    SimpleRouteConfig simpleRouteConfig;

    @BeforeEach
    void setUp() {
        coinGeckoSimpleApiServiceMock = mock(CoinGeckoSimpleApiService.class);
        apiServiceConfigMock = mock(ApiServiceConfig.class);

        handler = new SimpleApiHandler(coinGeckoSimpleApiServiceMock);

        when(apiServiceConfigMock.getBaseURL()).thenReturn("/api");

        SimpleApiHandler handler = new SimpleApiHandler(coinGeckoSimpleApiServiceMock);

        when(apiServiceConfigMock.getSimplePrice()).thenReturn("/simple/price");
        when(apiServiceConfigMock.getSimpleTokePriceById()).thenReturn("/simple/token_price");

        SimpleRouteConfig simpleRouteConfig = new SimpleRouteConfig(apiServiceConfigMock);
        webTestClient = WebTestClient.bindToRouterFunction(
                simpleRouteConfig.routeSimple(handler)).build(

        );
    }

    @Test
    void getSimplePriceFromCoinGeckoApi_Successfully() {
        Map<String, Double> searchMock = Instancio
                .create(new TypeToken<Map<String, Double>>() {
                });
        when(coinGeckoSimpleApiServiceMock.getSimplePriceApiService(any()))
                .thenReturn(Mono.just(searchMock));

        webTestClient.get()
                .uri(apiServiceConfigMock.getBaseURL() +
                        apiServiceConfigMock.getSimplePrice() +
                        "?ids=bitcoin&vsCurrencies=usd")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class).isEqualTo(searchMock);
    }

}
