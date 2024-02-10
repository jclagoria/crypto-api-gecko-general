package ar.com.api.general.handler;

import ar.com.api.general.configuration.ApiServiceConfig;
import ar.com.api.general.model.ExchangeRate;
import ar.com.api.general.router.ExchangeRateRouterConfig;
import ar.com.api.general.services.CoinGeckoServiceExchangeRatesApi;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExchangeRateApiHandlerTest {

    private WebTestClient webTestClient;
    private CoinGeckoServiceExchangeRatesApi exchangeRatesApiMock;
    private ApiServiceConfig apiServiceConfigMock;

    @BeforeEach
    void setUp() {
        exchangeRatesApiMock = mock(CoinGeckoServiceExchangeRatesApi.class);
        apiServiceConfigMock = mock(ApiServiceConfig.class);

        ExchangeRateApiHandler handler = new ExchangeRateApiHandler(exchangeRatesApiMock);

        when(apiServiceConfigMock.getBaseURL()).thenReturn("/api");
        when(apiServiceConfigMock.getExchangeRates()).thenReturn("/exchange_rates");

        ExchangeRateRouterConfig exchangeRateRouterConfig =
                new ExchangeRateRouterConfig(apiServiceConfigMock);
        webTestClient = WebTestClient
                .bindToRouterFunction(exchangeRateRouterConfig.routeExchangeRate(handler))
                .build();
    }

    @Test
    void getExchangeRateFromGeckoApi_Successfully() {
        ExchangeRate exchangeRateMock = Instancio.create(ExchangeRate.class);
        when(exchangeRatesApiMock.getExchangeRatesFromGeckoApi())
                .thenReturn(Mono.just(exchangeRateMock));

        webTestClient.get()
                .uri(
                        apiServiceConfigMock.getBaseURL() +
                        apiServiceConfigMock.getExchangeRates())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExchangeRate.class)
                    .isEqualTo(exchangeRateMock);
    }

    @Test
    void getExchangeRateFromGeckoApi_ReturnsBadRequest() {
        when(exchangeRatesApiMock.getExchangeRatesFromGeckoApi())
                .thenReturn(Mono.error(new IllegalArgumentException("Not Found")));

        webTestClient.get()
                .uri(apiServiceConfigMock.getBaseURL() )
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

}