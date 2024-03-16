package ar.com.api.general.handler

import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiClientErrorException
import ar.com.api.general.model.Ping
import ar.com.api.general.services.CoinGeckoServiceStatus
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class HealthApiHandlerTest extends Specification {

    CoinGeckoServiceStatus coinGeckoServiceStatusMock
    ServerRequest serverRequestMock
    HealthApiHandler apiHandler

    def setup() {
        coinGeckoServiceStatusMock = Mock(CoinGeckoServiceStatus)
        serverRequestMock = Mock(ServerRequest)

        apiHandler = new HealthApiHandler(coinGeckoServiceStatusMock)
    }

    def "GetStatusServiceCoinGecko return 200 Ok with expected body successfully response"() {
        given: "A mock CoinGeckoServiceStatus and a successfully Ping response"
        def expectedObject = Instancio.create(Ping.class)
        coinGeckoServiceStatusMock.getStatusCoinGeckoService() >> Mono.just(expectedObject)

        when: "CoinGeckoServiceStatus is called and return an Object"
        def actualResponseObject = apiHandler.getStatusServiceCoinGecko(serverRequestMock)

        then: "The response is 200 Ok with the expected body"
        StepVerifier.create(actualResponseObject)
                .assertNext {actualResponse ->
                    assert actualResponse.statusCode() == HttpStatus.OK: "Status should not be different to Ok"
                    assert actualResponse.headers().getContentType() == MediaType.APPLICATION_JSON: "ContentType should not be different to Application Json"
                }
                .verifyComplete()
    }

    def "GetStatusServiceCoinGecko return 404 Not Found for empty service response"() {
        given: "A mock GetStatusServiceCoinGecko and a empty response"
        coinGeckoServiceStatusMock.getStatusCoinGeckoService() >> Mono.empty()

        when: "GetStatusServiceCoinGecko is called adn return empty object"
        def actualEmptyObject = apiHandler.getStatusServiceCoinGecko(serverRequestMock)

        then: "The response expected is 404 not found"
        StepVerifier.create(actualEmptyObject)
                .assertNext {actualResponse ->
                    assert actualResponse.statusCode() == HttpStatus.NOT_FOUND: "StatusCode should by Not Found"
                    assert actualResponse.headers().isEmpty()
                }
                .verifyComplete()
    }

    def "GetStatusServiceCoinGecko handle error gracefully" () {
        given: "A mock GetStatusServiceCoinGecko and an error response"
        coinGeckoServiceStatusMock.getStatusCoinGeckoService() >>
                Mono.error(new RuntimeException("An error occurred"))

        when: "GetStatusServiceCoinGecko is called"
        def actualErrorResponse = apiHandler.getStatusServiceCoinGecko(serverRequestMock)

        then: "It handles the error and returns an internal server error"
        StepVerifier.create(actualErrorResponse)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiClientErrorException &&
                            actualError.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.API_SERVER_ERROR &&
                            actualError.getMessage() == "An expected error occurred in getStatusServiceCoinGecko"
                }
                .verify()
    }
}
