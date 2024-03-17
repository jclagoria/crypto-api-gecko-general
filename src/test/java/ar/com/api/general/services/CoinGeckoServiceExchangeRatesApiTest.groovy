package ar.com.api.general.services

import ar.com.api.general.configuration.ExternalServerConfig
import ar.com.api.general.configuration.HttpServiceCall
import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiServerErrorException
import ar.com.api.general.model.ExchangeRate
import ar.com.api.general.model.Rate
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class CoinGeckoServiceExchangeRatesApiTest extends Specification {

    HttpServiceCall httpServiceCallMock
    ExternalServerConfig externalServerConfigMock
    CoinGeckoServiceExchangeRatesApi coinGeckoServiceExchangeRatesApi

    def setup() {
        httpServiceCallMock = Mock(HttpServiceCall)
        externalServerConfigMock = Mock(ExternalServerConfig)

        externalServerConfigMock.getExchangeRates() >> "exchangeRateUrlMock"

        coinGeckoServiceExchangeRatesApi = new CoinGeckoServiceExchangeRatesApi(httpServiceCallMock,
                externalServerConfigMock)
    }

    def "GetExchangeRatesFromGeckoApi should successfully retrieve an Exchange Rate"() {
        given: "A mock setup for HttServiceCall and mocked object Exchange Rates"
        def expectedExchangeRates = Instancio.create(ExchangeRate.class)
        httpServiceCallMock.getMonoObject(_ as String, ExchangeRate.class) >> Mono.just(expectedExchangeRates)
        Map<String, Rate> rates = expectedExchangeRates.getRate();
        Optional<Map.Entry<String, Rate>> firstEntryOptional = rates.entrySet().stream().findFirst();
        Map.Entry<String, Rate> firstEntry = firstEntryOptional.get()
        String keyMock = firstEntry.getKey();

        when: "GetExchangeRatesFromGeckoApi is called, and then return Mono<ExchangeRate>"
        def actualObjectResponse = coinGeckoServiceExchangeRatesApi.getExchangeRatesFromGeckoApi();

        then: "The service return an Object successfully and validate the content"
        StepVerifier.create(actualObjectResponse)
                .assertNext { exchangeRateObject ->
                    assert exchangeRateObject.getRate() != null: "The expected object should not be null"
                    assert !exchangeRateObject.getRate().isEmpty(): "The expected object should not be empty"

                    assert exchangeRateObject.getRate().containsKey(keyMock): "The rate map should contain the key '$keyMock'"
                    assert exchangeRateObject.getRate().get(keyMock).getValue() != null: "The value for key '$keyMock' should not be null"
                }
                .verifyComplete()
    }

    def "GetExchangeRatesFromGeckoApi should handle 4xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall with a 4xx client error"
        def clientErrorExpected = new ApiServerErrorException("An error occurred in ApiClient",
                "Failed Dependency", ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.FAILED_DEPENDENCY)
        httpServiceCallMock.getMonoObject(_ as String, ExchangeRate.class) >> Mono.error(clientErrorExpected)

        when: "GetExchangeRatesFromGeckoApi is called and expect a 4xx error scenario"
        def actualExceptionObject = coinGeckoServiceExchangeRatesApi.getExchangeRatesFromGeckoApi()

        then: "The service return 4xx client error and verify"
        StepVerifier.create(actualExceptionObject)
                .expectErrorMatches { errorActual ->
                    errorActual instanceof ApiServerErrorException &&
                            errorActual.getErrorTypeEnum() == ErrorTypeEnum.GECKO_CLIENT_ERROR &&
                            errorActual.getHttpStatus().is4xxClientError() &&
                            errorActual.getMessage() == clientErrorExpected.getMessage() &&
                            errorActual.getLocalizedMessage() == clientErrorExpected.getLocalizedMessage()
                }
                .verify()
    }

    def "GetExchangeRatesFromGeckoApi should handle 5xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall with a 5xx client error"
        def clientErrorExpected = new ApiServerErrorException("An error occurred in ApiServer",
                "Internal Server Error", ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
        httpServiceCallMock.getMonoObject(_ as String, ExchangeRate.class) >> Mono.error(clientErrorExpected)

        when: "GetExchangeRatesFromGeckoApi is called and expect a 5xx error scenario"
        def actualExceptionObject = coinGeckoServiceExchangeRatesApi.getExchangeRatesFromGeckoApi()

        then: "The service return 5xx client error and verify"
        StepVerifier.create(actualExceptionObject)
                .expectErrorMatches { errorActual ->
                    errorActual instanceof ApiServerErrorException &&
                            errorActual.getErrorTypeEnum() == ErrorTypeEnum.GECKO_SERVER_ERROR &&
                            errorActual.getHttpStatus().is5xxServerError() &&
                            errorActual.getMessage() == clientErrorExpected.getMessage() &&
                            errorActual.getLocalizedMessage() == clientErrorExpected.getLocalizedMessage()
                }
                .verify()
    }

}
