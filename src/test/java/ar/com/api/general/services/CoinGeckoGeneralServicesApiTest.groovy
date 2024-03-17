package ar.com.api.general.services

import ar.com.api.general.configuration.ExternalServerConfig
import ar.com.api.general.configuration.HttpServiceCall
import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiServerErrorException
import ar.com.api.general.model.Global
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class CoinGeckoGeneralServicesApiTest extends Specification {

    HttpServiceCall httpServiceCallMock
    ExternalServerConfig externalServerConfigMock
    CoinGeckoGeneralServicesApi geckoGeneralServicesApi

    def setup() {
        httpServiceCallMock = Mock(HttpServiceCall)
        externalServerConfigMock = Mock(ExternalServerConfig)

        externalServerConfigMock.getGlobal() >> "globalInfoUrlMock"

        geckoGeneralServicesApi = new CoinGeckoGeneralServicesApi(httpServiceCallMock, externalServerConfigMock)
    }

    def "GetGlobalData should successfully retrieve an Global object"() {
        given: "A mock setup for HttpServiceCall and mocked object Global"
        def expectedGlobalObject = Instancio.create(Global.class)
        httpServiceCallMock.getMonoObject(_ as String, Global.class) >> Mono.just(expectedGlobalObject)

        when: "GetGlobalData is called, and then return Mono<Global>"
        def actualObjectResponse = geckoGeneralServicesApi.getGlobalData()

        then: "The service return an Object successfully and validate the content"
        StepVerifier.create(actualObjectResponse)
                .assertNext {globalObject ->
                    assert globalObject.getData() != null: "The Data should not be null"
                    assert globalObject.getData().getActiveCryptocurrencies() > 0L: "Active Cryptocurrencies should be mayor to zero"
                    assert !globalObject.getData().getMarketCapPercentage().isEmpty(): "Market Cap Percentage should not be empty"
                    assert globalObject.getData().getMarkets() > 0L: "Markets should be mayor to zero"
                    assert globalObject.getData().getEndedIcos() > 0L: "Ended Icos should be mayor to zero"
                }
                .verifyComplete()
    }

    def "GetGlobalData should handle 4xx client error gracefully"() {
        given: "A mock setup for HttpServerCall with a 4xx client error"
        def clientErrorException = new ApiServerErrorException("An error occurred in ApiClient", "Bad Request",
                ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.BAD_REQUEST)
        httpServiceCallMock.getMonoObject(_ as String, Global.class) >> Mono.error(clientErrorException)

        when: "GetGlobalData is called and expected a 4xx error scenario"
        def actualExceptionObject = geckoGeneralServicesApi.getGlobalData();

        then: "The service return 4xx client error and verify"
        StepVerifier.create(actualExceptionObject)
                .expectErrorMatches {errorActual ->
                    errorActual instanceof ApiServerErrorException &&
                            errorActual.getErrorTypeEnum() == ErrorTypeEnum.GECKO_CLIENT_ERROR &&
                            errorActual.getHttpStatus().is4xxClientError() &&
                            errorActual.getMessage() == clientErrorException.getMessage() &&
                            errorActual.getLocalizedMessage() == clientErrorException.getLocalizedMessage()
                }.verify()
    }

    def "GetGlobalData should handle 5xx client error gracefully"() {
        given: "A mock setup for HttpServerCall with a 5xx client error"
        def clientErrorException = new ApiServerErrorException("An error occurred in ApiServer", "Bad Request",
                ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
        httpServiceCallMock.getMonoObject(_ as String, Global.class) >> Mono.error(clientErrorException)

        when: "GetGlobalData is called and expected a 5xx error scenario"
        def actualExceptionObject = geckoGeneralServicesApi.getGlobalData();

        then: "The service return 5xx client error and verify"
        StepVerifier.create(actualExceptionObject)
                .expectErrorMatches {errorActual ->
                    errorActual instanceof ApiServerErrorException &&
                            errorActual.getErrorTypeEnum() == ErrorTypeEnum.GECKO_SERVER_ERROR &&
                            errorActual.getHttpStatus().is5xxServerError() &&
                            errorActual.getMessage() == clientErrorException.getMessage() &&
                            errorActual.getLocalizedMessage() == clientErrorException.getLocalizedMessage()
                }.verify()
    }

}
