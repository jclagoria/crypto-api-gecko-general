package ar.com.api.general.handler

import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiClientErrorException
import ar.com.api.general.model.Global
import ar.com.api.general.services.CoinGeckoGeneralServicesApi
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class GlobalApiHandlerTest extends Specification {

    CoinGeckoGeneralServicesApi coinGeckoGeneralServicesApiMock
    ServerRequest serverRequestMock
    GlobalApiHandler apiHandler

    def setup() {
        coinGeckoGeneralServicesApiMock = Mock(CoinGeckoGeneralServicesApi)
        serverRequestMock = Mock(ServerRequest)

        apiHandler = new GlobalApiHandler(coinGeckoGeneralServicesApiMock)
    }

    def "GetGlobalDataFromGeckoApi return successfully a ServerResponse with a HttpStatus Ok"() {
        given: "Mocked ServerResponse and Global mock object and GlobalApiHandler return HttpStatus Ok"
        def expectedObject = Instancio.create(Global.class)
        coinGeckoGeneralServicesApiMock.getGlobalData() >> Mono.just(expectedObject)

        when: "GetGlobalDataFromGeckoApi is called and return successfully ServerResponse"
        def actualResponseObject = apiHandler.getGlobalDataFromGeckoApi(serverRequestMock)

        then: "It returns a ServerResponse with HttpResponse 200 Ok"
        StepVerifier.create(actualResponseObject)
                .expectNextMatches { responseObject ->
                    responseObject.statusCode().is2xxSuccessful() &&
                            responseObject.headers().getContentType() == MediaType.APPLICATION_JSON
                }.verifyComplete()
    }

    def "GetGlobalDataFromGeckoApi returns not found when the service return from the API Service"() {
        given: "Mocked ServerRequest and GlobalApiHandler return Not Found"
        coinGeckoGeneralServicesApiMock.getGlobalData() >> Mono.empty()

        when: "GetGlobalDataFromGeckoApi is called and return a ServerResponse with Not Found Status"
        def actualResponseObject = apiHandler.getGlobalDataFromGeckoApi(serverRequestMock)

        then: "It returns a Not Found Response"
        StepVerifier.create(actualResponseObject)
                .expectNextMatches {response ->
                    response.statusCode() == HttpStatus.NOT_FOUND
                }
                .verifyComplete()
    }

    def "GetGlobalDataFromGeckoApi  handles and error gracefully"() {
        given: "Mocked ServerRequest and GlobalApiHandler return Error Scenario"
        coinGeckoGeneralServicesApiMock.getGlobalData() >>
                Mono.error(new RuntimeException("An error Occurred"))

        when: "GetGlobalDataFromGeckoApi is called and return a ServerRequest with a error scenario"
        def actualErrorObject = apiHandler.getGlobalDataFromGeckoApi(serverRequestMock)

        then: "It handles the error and returns an Internal Server Error"
        StepVerifier.create(actualErrorObject)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiClientErrorException &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.API_SERVER_ERROR &&
                            actualError.getMessage() == "An unexpected error occurred in getGlobalDataFromGeckoApi"
                }
                .verify()
    }

}