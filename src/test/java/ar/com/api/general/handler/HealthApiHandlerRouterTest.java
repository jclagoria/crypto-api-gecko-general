package ar.com.api.general.handler;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.model.Ping;
import ar.com.api.general.router.HealthRouterConfig;
import ar.com.api.general.services.CoinGeckoServiceStatus;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HealthApiHandlerRouterTest {
    private WebTestClient webTestClient;
    private CoinGeckoServiceStatus coinGeckoServiceStatusMock;
    private ApiServiceConfig apiServiceConfigMock;

    @BeforeEach
    void setUp() {
        coinGeckoServiceStatusMock = mock(CoinGeckoServiceStatus.class);
        apiServiceConfigMock = mock(ApiServiceConfig.class);

        HealthApiHandler handler = new HealthApiHandler(coinGeckoServiceStatusMock);

        when(apiServiceConfigMock.getBaseURL()).thenReturn("/api");
        when(apiServiceConfigMock.getHealthAPI()).thenReturn("/health");

        HealthRouterConfig healthRouterConfig = new HealthRouterConfig(apiServiceConfigMock);

        webTestClient = WebTestClient
                .bindToRouterFunction(healthRouterConfig.route(handler)).build();
    }

    @Test
    void getStatusServiceCoinGecko_Successfully() {
        Ping pingMock = Instancio.create(Ping.class);
        when(coinGeckoServiceStatusMock.getStatusCoinGeckoService())
                .thenReturn(Mono.just(pingMock));

        webTestClient.get()
                .uri(
                        apiServiceConfigMock.getBaseURL() +
                                apiServiceConfigMock.getHealthAPI()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ping.class).isEqualTo(pingMock);
    }

}