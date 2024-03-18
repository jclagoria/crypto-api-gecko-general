package ar.com.api.general.services

import ar.com.api.general.configuration.ExternalServerConfig
import ar.com.api.general.configuration.HttpServiceCall
import ar.com.api.general.dto.SearchDTO
import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiServerErrorException
import ar.com.api.general.model.Search
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class CoinGeckoSearchAPIServiceTest extends Specification {

    HttpServiceCall httpServiceCallMock;
    ExternalServerConfig externalServerConfigMock
    CoinGeckoSearchAPIService searchAPIService

    def setup() {
        httpServiceCallMock = Mock(HttpServiceCall)
        externalServerConfigMock = Mock(ExternalServerConfig)

        externalServerConfigMock.getSearch() >> "searchUrlMock"

        searchAPIService = new CoinGeckoSearchAPIService(httpServiceCallMock, externalServerConfigMock)
    }

    def "GetSearchFromGeckoApi should successfully retrieve an Search"() {
        given: "A mock setup for HttpServiceCall and mocked Filter object and Search mocked object"
        def filterDTO = Instancio.create(SearchDTO.class)
        def expectedObject = Instancio.create(Search.class)
        httpServiceCallMock.getMonoObject(_ as String, Search.class) >> Mono.just(expectedObject)

        when: "GetSearchFromGeckoApi is called with a filter mock and then return Mono<Search>"
        def actualObjectResponse = searchAPIService.getSearchFromGeckoApi(filterDTO)

        then: "The service return an Object successfully and validate the content"
        StepVerifier.create(actualObjectResponse)
                .assertNext {searchObject ->
                    assert !searchObject.getCoinList().isEmpty(): "The List of Coins should not be null or empty"
                    assert !searchObject.getCategoriesList().isEmpty(): "The List of Categories should not be null or empty"
                    assert !searchObject.getExhcangeList().isEmpty(): "The List of Exchanges should not be null or empty"
                    assert !searchObject.getNtfsList().isEmpty(): "The List of NFTS should not be null or empty"

                    assert searchObject.getCoinList().size() == expectedObject.getCoinList().size(): "List of Coin - The number of item in actual object should be equals to the items in the expected object"
                    assert searchObject.getCategoriesList().size() == expectedObject.getCategoriesList().size(): "List of Categories - The number of item in actual object should be equals to the items in the expected object"
                    assert searchObject.getExhcangeList().size() == expectedObject.getExhcangeList().size(): "List of Exchange - The number of item in actual object should be equals to the items in the expected object"
                    assert searchObject.getNtfsList().size() == expectedObject.getNtfsList().size(): "List of Nfts - The number of item in actual object should be equals to the items in the expected object"

                    assert searchObject.getCoinList().containsAll(expectedObject.getCoinList()): "The elements in coin list of actual should be has in the expected List"
                    assert searchObject.getNtfsList().containsAll(expectedObject.getNtfsList()): "The elements in nfts list of actual should be has in the expected List"
                    assert searchObject.getExhcangeList().containsAll(expectedObject.getExhcangeList()): "The elements in exchange list of actual should be has in the expected List"
                    assert searchObject.getCategoriesList().containsAll(expectedObject.getCategoriesList()): "The elements in categories list of actual should be has in the expected List"
                }
                .verifyComplete()

    }

    def "GetSearchFromGeckoApi should handle 4xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall with a 4xx client error and mocked Filter object"
        def filterDTO = Instancio.create(SearchDTO.class)
        def clientErrorExpected = new ApiServerErrorException("An error occurred in ApiClient",
                "Forbidden", ErrorTypeEnum.GECKO_CLIENT_ERROR, HttpStatus.FORBIDDEN)
        httpServiceCallMock.getMonoObject(_ as String, Search.class) >> Mono.error(clientErrorExpected)

        when: "GetSearchFromGeckoApi is called and expect a 4xx error scenario"
        def actualErrorResponse = searchAPIService.getSearchFromGeckoApi(filterDTO)

        then: "The service return 4xx client error and verify"
        StepVerifier.create(actualErrorResponse)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiServerErrorException &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.GECKO_CLIENT_ERROR &&
                            actualError.getMessage() == clientErrorExpected.getMessage() &&
                            actualError.getLocalizedMessage() == clientErrorExpected.getLocalizedMessage() &&
                            actualError.getHttpStatus().is4xxClientError()
                }
                .verify()
    }

    def "GetSearchFromGeckoApi should handle 5xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall with a 5xx client error and mocked Filter object"
        def filterDTO = Instancio.create(SearchDTO.class)
        def clientErrorExpected = new ApiServerErrorException("An error occurred in ApiServer",
                "Bad Gateway", ErrorTypeEnum.GECKO_SERVER_ERROR, HttpStatus.BAD_GATEWAY)
        httpServiceCallMock.getMonoObject(_ as String, Search.class) >> Mono.error(clientErrorExpected)

        when: "GetSearchFromGeckoApi is called and expect a 4xx error scenario"
        def actualErrorResponse = searchAPIService.getSearchFromGeckoApi(filterDTO)

        then: "The service return 4xx client error and verify"
        StepVerifier.create(actualErrorResponse)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiServerErrorException &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.GECKO_SERVER_ERROR &&
                            actualError.getMessage() == clientErrorExpected.getMessage() &&
                            actualError.getLocalizedMessage() == clientErrorExpected.getLocalizedMessage() &&
                            actualError.getHttpStatus().is5xxServerError()
                }
                .verify()
    }


}
