package ar.com.api.general.handler

import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiClientErrorException
import ar.com.api.general.model.ExchangeRate
import ar.com.api.general.services.CoinGeckoServiceExchangeRatesApi
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class ExchangeRateApiHandlerTest extends Specification {

    CoinGeckoServiceExchangeRatesApi serviceExchangeRatesApiMock
    ServerRequest serverRequestMock
    ExchangeRateApiHandler apiHandler

    def setup() {
        serviceExchangeRatesApiMock = Mock(CoinGeckoServiceExchangeRatesApi)
        serverRequestMock = Mock(ServerRequest)

        apiHandler = new ExchangeRateApiHandler(serviceExchangeRatesApiMock)
    }

    def "GetExchangeRateFromGeckoApi return successfully a ServerRequest with HttpStatus Ok"() {
        given: "Mocked ServerRequest and ExchangeRate and ExchangeRateApiHandler return HttpStatus Ok"
        def expectedObject = Instancio.create(ExchangeRate.class)
        serviceExchangeRatesApiMock.getExchangeRatesFromGeckoApi() >> Mono.just(expectedObject)

        when: "GetExchangeRateFromGeckoApi is called and return successfully ServerResponse"
        def actualResponse = apiHandler.getExchangeRateFromGeckoApi(serverRequestMock)

        then: "It returns a ServerResponse with HttpResponse 200 Ok"
        StepVerifier.create(actualResponse)
                .expectNextMatches {response ->
                    response.statusCode().is2xxSuccessful() &&
                            response.headers().getContentType() == MediaType.APPLICATION_JSON
                }
                .verifyComplete()
    }

    def "GetExchangeRateFromGeckoApi returns not found when the service return from the API Service"() {
        given: "Mocked ServerRequest and ExchangeRate and ExchangeRateApiHandler return Not Found"
        serviceExchangeRatesApiMock.getExchangeRatesFromGeckoApi() >> Mono.empty()

        when: "GetExchangeRateFromGeckoApi is called and return Not Found"
        def actualResponseObject = apiHandler.getExchangeRateFromGeckoApi(serverRequestMock)

        then: "It returns a not found response"
        StepVerifier.create(actualResponseObject)
                .expectNextMatches {response ->
                    response.statusCode() == HttpStatus.NOT_FOUND
                }
                .verifyComplete()
    }

    def "GetExchangeRateFromGeckoApi handles and error gracefully"() {
        given: "Mocked ServerRequest and ExchangeRate and ExchangeRateApiHandler return Error Scenario"
        serviceExchangeRatesApiMock.getExchangeRatesFromGeckoApi() >>
                Mono.error(new RuntimeException("An error occurred"))

        when: "GetExchangeRateFromGeckoApi is called and return successfully an ServerRequest"
        def actualErrorObject = apiHandler.getExchangeRateFromGeckoApi(serverRequestMock)

        then: "It handles the error and returns an Internal Server Error"
        StepVerifier.create(actualErrorObject)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiClientErrorException &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.API_SERVER_ERROR &&
                            actualError.getMessage() == "An unexpected error occurred in getExchangeRateFromGeckoApi"
                }
                .verify()
    }

}
