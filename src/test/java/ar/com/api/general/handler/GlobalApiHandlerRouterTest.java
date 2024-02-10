package ar.com.api.general.handler;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.model.DecentralizedFinance;
import ar.com.api.general.model.Global;
import ar.com.api.general.router.GlobalRouterConfig;
import ar.com.api.general.services.CoinGeckoGeneralServicesApi;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

public class GlobalApiHandlerRouterTest {

    private WebTestClient webTestClient;
    private CoinGeckoGeneralServicesApi generalServiceMock;
    private ApiServiceConfig apiServiceConfigMock;

    @BeforeEach
    void setUp() {
        generalServiceMock = Mockito.mock(CoinGeckoGeneralServicesApi.class);
        apiServiceConfigMock = Mockito.mock(ApiServiceConfig.class);
        GlobalApiHandler handler = new GlobalApiHandler(generalServiceMock);

        when(apiServiceConfigMock.getBaseURL()).thenReturn("/api");
        when(apiServiceConfigMock.getGlobal()).thenReturn("/global");
        when(apiServiceConfigMock.getDecentralized()).thenReturn("/defi");

        GlobalRouterConfig routerConfig = new GlobalRouterConfig(apiServiceConfigMock);

        webTestClient = WebTestClient.bindToRouterFunction(routerConfig.routeGlobal(handler)).build();
    }

    @Test
    public void getGlobalDataFromGeckoApi_Success() {
        Global mockGlobal = Instancio.create(Global.class);
        when(generalServiceMock.getGlobalData()).thenReturn(Mono.just(mockGlobal));

        webTestClient.get().uri("/api/global")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Global.class).isEqualTo(mockGlobal);
    }

    @Test
    public void getDecentralizedFinanceDefi_Success() {
        DecentralizedFinance mockDefi = Instancio.create(DecentralizedFinance.class);
        when(generalServiceMock.getDecentralizedFinance()).thenReturn(Mono.just(mockDefi));

        webTestClient.get().uri("/api/defi")
                .exchange()
                .expectStatus().isOk()
                .expectBody(DecentralizedFinance.class).isEqualTo(mockDefi);
    }
}