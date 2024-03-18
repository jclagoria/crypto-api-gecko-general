package ar.com.api.general.handler

import ar.com.api.general.dto.SimplePriceFilterDTO
import ar.com.api.general.entity.DataMapTest
import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiClientErrorException
import ar.com.api.general.services.CoinGeckoSimpleApiService
import ar.com.api.general.validator.ValidatorOfCTOComponent
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class SimpleApiHandlerTest extends Specification {

    CoinGeckoSimpleApiService coinGeckoSimpleApiServiceMock
    ServerRequest serverRequestMock
    ValidatorOfCTOComponent validatorMock
    SimpleApiHandler apiHandler

    def setup() {
        coinGeckoSimpleApiServiceMock = Mock(CoinGeckoSimpleApiService)
        serverRequestMock = Mock(ServerRequest)
        validatorMock = Mock(ValidatorOfCTOComponent)

        apiHandler = new SimpleApiHandler(coinGeckoSimpleApiServiceMock, validatorMock)
    }

    def "GetSimplePriceFromCoinGeckoApi return successfully a ServerResponse with a HttpStatus Ok"() {
        given: "Mocked ServerResponse and Mocked ValidatorComponent and Mocked ApiService and Mocked SimpleApiHandler"
        def filterDTO = Instancio.create(SimplePriceFilterDTO.class)
        def expectedObject = Instancio
                .ofMap(String.class, DataMapTest.class).size(1).create()
        serverRequestMock.queryParam(_ as String) >> Optional.of("bitcoin")
        serverRequestMock.queryParam(_ as String) >> Optional.of("usd,jyp")
        validatorMock.validation(_) >> Mono.just(filterDTO)
        coinGeckoSimpleApiServiceMock.getSimplePriceApiService(_ as SimplePriceFilterDTO) >> Mono.just(expectedObject)

        when: "GetSimplePriceFromCoinGeckoApi is called and return successfully ServerResponse"
        def actualResponseObject = apiHandler.getSimplePriceFromCoinGeckoApi(serverRequestMock)

        then: "It returns a ServerResponse with HttpResponse 200 Ok"
        StepVerifier.create(actualResponseObject)
                .expectNextMatches {responseObject ->
                    responseObject.statusCode().is2xxSuccessful() &&
                            responseObject.headers().getContentType() == MediaType.APPLICATION_JSON
                }
                .verifyComplete()
    }

    def "GetSimplePriceFromCoinGeckoApi  returns not found when the service return from the API Service"() {
        given: "Mocked ServerResponse and Mocked ValidatorComponent and Mocked ApiService and Mocked SimpleApiHandler to return Not Found"
        def filterDTO = Instancio.create(SimplePriceFilterDTO.class)
        serverRequestMock.queryParam(_ as String) >> Optional.of("bitcoin")
        serverRequestMock.queryParam(_ as String) >> Optional.of("aus")
        validatorMock.validation(_) >> Mono.just(filterDTO)
        coinGeckoSimpleApiServiceMock.getSimplePriceApiService(_ as SimplePriceFilterDTO) >> Mono.empty()

        when: "GetSimplePriceFromCoinGeckoApi is called and return a not found ServerResponse"
        def actualResponseNotFound = apiHandler.getSimplePriceFromCoinGeckoApi(serverRequestMock)

        then: "It returns a Not Found Response and validate"
        StepVerifier.create(actualResponseNotFound)
                .expectNextMatches {response ->
                    response.statusCode() == HttpStatus.NOT_FOUND
                }
                .verifyComplete()
    }

    def "GetSimplePriceFromCoinGeckoApi handles and error gracefully"() {
        given: "Mocked ServerRequest and ApiService return Error Scenario"
        coinGeckoSimpleApiServiceMock.getSimplePriceApiService(_ as SimplePriceFilterDTO) >>
                Mono.error(new RuntimeException("An error Occurred"))

        when: "GetSimplePriceFromCoinGeckoApi is called and return Internal Server Error"
        def actualErrorObject = apiHandler.getSimplePriceFromCoinGeckoApi(serverRequestMock)

        then: "It handles the error and validate if is an Internal Server Error"
        StepVerifier.create(actualErrorObject)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiClientErrorException &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.API_SERVER_ERROR &&
                            actualError.getHttpStatus().is5xxServerError() &&
                            actualError.getMessage() == "An unexpected error occurred in getSimplePriceFromCoinGeckoApi"
                }
                .verify()
    }

}
