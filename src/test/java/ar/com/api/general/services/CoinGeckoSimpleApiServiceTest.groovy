package ar.com.api.general.services

import ar.com.api.general.configuration.ExternalServerConfig
import ar.com.api.general.configuration.HttpServiceCall
import ar.com.api.general.dto.SimplePriceFilterDTO
import ar.com.api.general.dto.TokenPriceByIdDTO
import ar.com.api.general.entity.DataMapTest
import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiServerErrorException
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class CoinGeckoSimpleApiServiceTest extends Specification {

    HttpServiceCall httpServiceCallMock
    ExternalServerConfig externalServerConfigMock
    CoinGeckoSimpleApiService simpleApiService

    def setup() {
        httpServiceCallMock = Mock(HttpServiceCall)
        externalServerConfigMock = Mock(ExternalServerConfig)

        externalServerConfigMock.getSimplePrice() >> "simplePriceUrlMock"
        externalServerConfigMock.getSimpleTokePriceById() >> "simpleTokenUrlMock"

        simpleApiService = new CoinGeckoSimpleApiService(httpServiceCallMock, externalServerConfigMock)
    }

    def "GetSimplePriceApiService should successfully retrieve a Mono<Map>"() {
        given: "A mock setup for HttpServiceCall and mocked object Map"
        def filterDTO = Instancio.create(SimplePriceFilterDTO.class)
        def expectedObject = Instancio.ofMap(String.class, DataMapTest.class).size(1).create()
        httpServiceCallMock.getMonoObject(_ as String, Map.class) >> Mono.just(expectedObject)

        when: "GetSimplePriceApiService is called and the return Mono<Map<String, Map<String, Double>>>"
        def actualObjectResponse = simpleApiService.getSimplePriceApiService(filterDTO)

        then: "The service return an Object successfully and validate the content"
        StepVerifier.create(actualObjectResponse)
                .assertNext {actualObject ->
                    assert actualObject != null: "The Map with hte dat should not be null"
                    assert actualObject.values().containsAll(expectedObject.values()): "The content in the actual object should be the same in the expectedObject"
                }
                .verifyComplete()
    }

    def "GetSimplePriceApiService should handle 4xx client error gracefully"() {
        given: "A mock setup for HttpServerCall with a 4xx client error"
        def filterDTO = Instancio.create(SimplePriceFilterDTO)
        def expectedErrorObject = new ApiServerErrorException("An error occurred in ApiClient",
                "Forbidden", ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.FORBIDDEN)
        httpServiceCallMock.getMonoObject(_ as String, Map.class) >> Mono.error(expectedErrorObject)

        when: "GetSimplePriceApiService is called and expected a 4xx error scenario"
        def actualExceptionObject = simpleApiService.getSimplePriceApiService(filterDTO)

        then: "The service return 4xx client error and verify"
        StepVerifier.create(actualExceptionObject)
                .expectErrorMatches {errorActual ->
                    errorActual instanceof ApiServerErrorException &&
                            errorActual.getErrorTypeEnum() == ErrorTypeEnum.GECKO_CLIENT_ERROR &&
                            errorActual.getHttpStatus().is4xxClientError() &&
                            errorActual.getMessage() == expectedErrorObject.getMessage() &&
                            errorActual.getLocalizedMessage() == expectedErrorObject.getLocalizedMessage()
                }
                .verify()
    }

    def "GetSimplePriceApiService should handle 5xx client error gracefully"() {
        given: "A mock setup for HttpServerCall with a 5xx client error"
        def filterDTO = Instancio.create(SimplePriceFilterDTO)
        def expectedErrorObject = new ApiServerErrorException("An error occurred in ApiServer",
                "Gateway Timeout", ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.GATEWAY_TIMEOUT)
        httpServiceCallMock.getMonoObject(_ as String, Map.class) >> Mono.error(expectedErrorObject)

        when: "GetSimplePriceApiService is called and expected a 5xx error scenario"
        def actualExceptionObject = simpleApiService.getSimplePriceApiService(filterDTO)

        then: "The service return 5xx client error and verify"
        StepVerifier.create(actualExceptionObject)
                .expectErrorMatches {errorActual ->
                    errorActual instanceof ApiServerErrorException &&
                            errorActual.getErrorTypeEnum() == ErrorTypeEnum.GECKO_SERVER_ERROR &&
                            errorActual.getHttpStatus().is5xxServerError() &&
                            errorActual.getMessage() == expectedErrorObject.getMessage() &&
                            errorActual.getLocalizedMessage() == expectedErrorObject.getLocalizedMessage()
                }
                .verify()
    }

    def "GetSimplePriceTokenById should successfully retrieve a Mono<Map>"() {
        given: "A mock setup for HttpServiceCall and mocked object Map"
        def filterDTO = Instancio.create(TokenPriceByIdDTO.class)
        def expectedObject = Instancio.ofMap(String.class, DataMapTest.class).size(2).create()
        httpServiceCallMock.getMonoObject(_ as String, Map.class) >> Mono.just(expectedObject)

        when: "GetSimplePriceTokenById is called and the return Mono<Map<String, Map<String, Double>>>"
        def actualObjectResponse = simpleApiService.getSimplePriceTokenById(filterDTO)

        then: "The service return an Object successfully and validate the content"
        StepVerifier.create(actualObjectResponse)
                .assertNext {actualObject ->
                    assert !actualObject.isEmpty(): "The Map with hte dat should not be null"
                    assert actualObject.values().containsAll(expectedObject.values()): "The content in the actual object should be the same in the expectedObject"
                }
                .verifyComplete()
    }

    def "GetSimplePriceTokenById should handle 4xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall and 4xx api error"
        def filterDTO = Instancio.create(TokenPriceByIdDTO.class)
        def expectedErrorObject = new ApiServerErrorException("An error occurred in ApiClient",
                "Method Not Allowed", ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.METHOD_NOT_ALLOWED)
        httpServiceCallMock.getMonoObject(_ as String, Map.class) >> Mono.error(expectedErrorObject)

        when: "GetSimplePriceTokenById is called and expected a 4xx error scenario"
        def actualErrorResponse = simpleApiService.getSimplePriceTokenById(filterDTO)

        then: "The service return 4xx client error and verify"
        StepVerifier.create(actualErrorResponse)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiServerErrorException &&
                            actualError.getMessage() == expectedErrorObject.getMessage() &&
                            actualError.getLocalizedMessage() == expectedErrorObject.getLocalizedMessage() &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.GECKO_CLIENT_ERROR &&
                            actualError.getHttpStatus().is4xxClientError()
                }
                .verify()

    }

    def "GetSimplePriceTokenById should handle 5xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall and 5xx api error"
        def filterDTO = Instancio.create(TokenPriceByIdDTO.class)
        def expectedErrorObject = new ApiServerErrorException("An error occurred in ApiServer",
                "Service Unavailable", ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.SERVICE_UNAVAILABLE)
        httpServiceCallMock.getMonoObject(_ as String, Map.class) >> Mono.error(expectedErrorObject)

        when: "GetSimplePriceTokenById is called and expected a 5xx error scenario"
        def actualErrorResponse = simpleApiService.getSimplePriceTokenById(filterDTO)

        then: "The service return 5xx client error and verify"
        StepVerifier.create(actualErrorResponse)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiServerErrorException &&
                            actualError.getMessage() == expectedErrorObject.getMessage() &&
                            actualError.getLocalizedMessage() == expectedErrorObject.getLocalizedMessage() &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.GECKO_SERVER_ERROR &&
                            actualError.getHttpStatus().is5xxServerError()
                }
                .verify()

    }

}
