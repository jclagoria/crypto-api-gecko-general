package ar.com.api.general.services

import ar.com.api.general.configuration.ExternalServerConfig
import ar.com.api.general.configuration.HttpServiceCall
import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiServerErrorException
import ar.com.api.general.model.Ping
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.Instant

class CoinGeckoServiceStatusTest extends Specification {

    HttpServiceCall httpServiceCallMock
    ExternalServerConfig externalServerConfigMock
    CoinGeckoServiceStatus coinGeckoServiceStatus

    def setup() {
        httpServiceCallMock = Mock(HttpServiceCall)
        externalServerConfigMock = Mock(ExternalServerConfig)

        externalServerConfigMock.getPing() >> "pingUrlServiceMock"

        coinGeckoServiceStatus = new CoinGeckoServiceStatus(httpServiceCallMock, externalServerConfigMock)
    }

    def "GetStatusCoinGeckoService should successfully retrieve service status"() {
        given: "A mock setup for HttpServiceCall and ExternalServerConfig"
        def expectedPingObject = Instancio.create(Ping.class)
        httpServiceCallMock.getMonoObject(_ as String, Ping.class) >> Mono.just(expectedPingObject)

        when: "GetStatusCoinGeckoService is called without DTO"
        def actualObject = coinGeckoServiceStatus.getStatusCoinGeckoService()

        then: "The service returns the expected Ping object"
        StepVerifier.create(actualObject)
                .assertNext {pingObject ->
                    assert pingObject.getGeckoSays() != null: "Ping should not be null"
                    assert pingObject.getGeckoSays() == expectedPingObject.getGeckoSays(): "Gecko says should not be different"
                }
                .verifyComplete()
    }

    def "GetStatusCoinGeckoService should handle 4xx client error gracefully"() {
        given: "A mock setup HttpServiceCall and ExternalServiceConfig and return 4xx client error"
        def expectedApiClientError = new ApiServerErrorException("An error occurred on APIClient",
                "Bad Request", ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.BAD_REQUEST)
        httpServiceCallMock.getMonoObject(_ as String, Ping.class) >> Mono.error(expectedApiClientError)

        when: "GetStatusCoinGeckoService is called with a 4xx error scenario"
        def actualErrorObject = coinGeckoServiceStatus.getStatusCoinGeckoService()

        then: "The service gracefully handle the error"
        StepVerifier.create(actualErrorObject)
                .expectErrorMatches {errorObject ->
                    errorObject instanceof ApiServerErrorException &&
                            errorObject.getHttpStatus().is4xxClientError() &&
                            errorObject.getErrorTypeEnum() == ErrorTypeEnum.GECKO_CLIENT_ERROR &&
                            errorObject.getOriginalMessage() == expectedApiClientError.getOriginalMessage() &&
                            errorObject.getMessage() == expectedApiClientError.getMessage()
                }
                .verify()
    }

    def "GetStatusCoinGeckoService should handle 5xx client error gracefully"() {
        given: "A mock setup HttpServiceCall and ExternalServerConfig and return server error"
        def expectedApiServerError = new ApiServerErrorException("An error occurred on APIServer",
                "Internal Server Error", ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR)
        httpServiceCallMock.getMonoObject(_ as String, Ping.class) >> Mono.error(expectedApiServerError)

        when: "GetStatusCoinGeckoService is called with a 5xx error scenario"
        def actualErrorObject = coinGeckoServiceStatus.getStatusCoinGeckoService()

        then: "The service gracefully handle the error"
        StepVerifier.create(actualErrorObject)
                .expectErrorMatches {errorObject ->
                    errorObject instanceof ApiServerErrorException &&
                            errorObject.getHttpStatus().is5xxServerError() &&
                            errorObject.getErrorTypeEnum() == ErrorTypeEnum.GECKO_SERVER_ERROR &&
                            errorObject.getOriginalMessage() == expectedApiServerError.getOriginalMessage() &&
                            errorObject.getMessage() == expectedApiServerError.getMessage()
                }
                .verify()
    }

}
