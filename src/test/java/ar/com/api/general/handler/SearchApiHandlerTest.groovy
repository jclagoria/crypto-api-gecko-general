package ar.com.api.general.handler

import ar.com.api.general.dto.SearchDTO
import ar.com.api.general.enums.ErrorTypeEnum
import ar.com.api.general.exception.ApiClientErrorException
import ar.com.api.general.model.Search
import ar.com.api.general.model.Trending
import ar.com.api.general.services.CoinGeckoSearchAPIService
import ar.com.api.general.validator.ValidatorOfCTOComponent
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class SearchApiHandlerTest extends Specification {

    CoinGeckoSearchAPIService coinGeckoSearchAPIServiceMock
    ServerRequest serverRequestMock
    ValidatorOfCTOComponent validatorOfCTOComponentMock

    SearchApiHandler apiHandler

    def setup() {
        coinGeckoSearchAPIServiceMock = Mock(CoinGeckoSearchAPIService)
        serverRequestMock = Mock(ServerRequest)
        validatorOfCTOComponentMock = Mock(ValidatorOfCTOComponent)

        apiHandler = new SearchApiHandler(coinGeckoSearchAPIServiceMock, validatorOfCTOComponentMock)
    }

    def "GetListOfCoinsWithSearchAPI return successfully a ServerRequest with HttpStatus Ok"() {
        given: "Mocked ServerRequest and mocked CoinGeckoSearchAPIService and mocked ValidatorOfCTOComponent and SearchApiHandler return HttpStatus Ok"
        def expectedObject = Instancio.create(Search.class)
        def filterDTO = Instancio.create(SearchDTO.class)
        serverRequestMock.queryParam(_ as String) >> Optional.of("bitcoin")
        validatorOfCTOComponentMock.validation(_) >> Mono.just(filterDTO)
        coinGeckoSearchAPIServiceMock.getSearchFromGeckoApi(_ as SearchDTO) >> Mono.just(expectedObject)

        when: "GetListOfCoinsWithSearchAPI is called and return successfully ServerRequest"
        def actualResponseObject = apiHandler.getListOfCoinsWithSearchAPI(serverRequestMock)

        then: "It returns a ServerResponse with HttpStatus 200"
        StepVerifier.create(actualResponseObject)
                .assertNext { actualResponse ->
                    assert actualResponse.statusCode() == HttpStatus.OK: "The HttpStatus code should bt be different to HttpStatus Ok"
                    assert actualResponse.headers().getContentType() == MediaType.APPLICATION_JSON: "The Content Type should not be different to Application Json"
                }
                .verifyComplete()
    }

    def "GetListOfCoinsWithSearchAPI return 404 Not Found for empty service response"() {
        given: "Mocked ServerRequest and mocked ValidatorOfCTOComponent and SearchApiHandler return HttpStatus Not Found"
        def filterDTO = Instancio.create(SearchDTO.class)
        serverRequestMock.queryParam(_ as String) >> Optional.of("usdt")
        validatorOfCTOComponentMock.validation(_) >> Mono.just(filterDTO)
        coinGeckoSearchAPIServiceMock.getSearchFromGeckoApi(filterDTO) >> Mono.empty()

        when: "GetListOfCoinsWithSearchAPI is called and return 404 Not Found"
        def actualEmptyObject = apiHandler.getListOfCoinsWithSearchAPI(serverRequestMock)

        then: "It returns a ServerResponse with HttpStatus 404 and verify"
        StepVerifier.create(actualEmptyObject)
                .assertNext { actualResponse ->
                    assert actualResponse.statusCode() == HttpStatus.NOT_FOUND: "StatusCode should by Not Found"
                    assert actualResponse.headers().isEmpty()
                }
                .verifyComplete()
    }

    def "GetListOfCoinsWithSearchAPI handler error successfully"() {
        given: "Mocked ServerRequest and mocked ValidatorOfCTOComponent and GetListOfCoinsWithSearchAPI to return a ApiClient error"
        def filterDTO = Instancio.create(SearchDTO.class)
        serverRequestMock.queryParam(_ as String) >> Optional.of("usdt")
        validatorOfCTOComponentMock.validation(_) >> Mono.just(filterDTO)
        coinGeckoSearchAPIServiceMock.getSearchFromGeckoApi(_ as SearchDTO) >>
                Mono.error(new RuntimeException("An error occurred"))

        when: "GetListOfCoinsWithSearchAPI is called and return a error scenario"
        def actualErrorObject = apiHandler.getListOfCoinsWithSearchAPI(serverRequestMock)

        then: "It handles the error an Internal Server Error and verify the content"
        StepVerifier.create(actualErrorObject)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiClientErrorException &&
                            actualError.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.API_SERVER_ERROR &&
                            actualError.getMessage() == "An unexpected error occurred in getListOfCoinsWithSearchAPI"
                }
                .verify()

    }

    def "GetTrendingOfCoinsAPI return successfully a ServerRequest with HttpStatus Ok"() {
        given: "Mocked ServerRequest and mocked CoinGeckoSearchAPIService and SearchApiHandler return HttpStatus Ok"
        def expectedObject = Instancio.create(Trending.class)
        coinGeckoSearchAPIServiceMock.getSearchTrendingFromGeckoApi() >> Mono.just(expectedObject)

        when: "GetTrendingOfCoinsAPI is called and return successfully ServerResponse"
        def actualResponseObject = apiHandler.getTrendingOfCoinsAPI(serverRequestMock)

        then: "It returns a ServerResponse with HttpStatus 200"
        StepVerifier.create(actualResponseObject)
                .assertNext {actualResponse ->
                    assert actualResponse.statusCode() == HttpStatus.OK: "The HttpStatus code should bt be different to HttpStatus Ok"
                    assert actualResponse.headers().getContentType() == MediaType.APPLICATION_JSON: "The Content Type should not be different to Application Json"
                }
                .verifyComplete()

    }

    def "GetTrendingOfCoinsAPI return 404 Not Found for empty service response"() {
        given: "Mocked SearchApiHandler return HttpStatus Not Found"
        coinGeckoSearchAPIServiceMock.getSearchTrendingFromGeckoApi() >> Mono.empty()

        when: "GetTrendingOfCoinsAPI is called and return 404 Not Found"
        def actualEmptyObject = apiHandler.getTrendingOfCoinsAPI(serverRequestMock)

        then: "It returns a ServerResponse with HttpStatus 404 and verify"
        StepVerifier.create(actualEmptyObject)
                .assertNext {actualResponse ->
                    assert actualResponse.statusCode() == HttpStatus.NOT_FOUND: "StatusCode should not be different to Not Found"
                    assert actualResponse.headers().isEmpty()
                }
                .verifyComplete()
    }

    def "GetTrendingOfCoinsAPI handle error successfully"() {
        given: "Mocked SearchApiHandler return HttpStatus Internal Server Error"
        coinGeckoSearchAPIServiceMock.getSearchTrendingFromGeckoApi() >>
                Mono.error(new RuntimeException("An error occurred"))

        when: "GetTrendingOfCoinsAPI is called and return a error scenario"
        def actualErrorResponse = apiHandler.getTrendingOfCoinsAPI(serverRequestMock)

        then: "It handles the error an Internal Server Error and verify the content"
        StepVerifier.create(actualErrorResponse)
                .expectErrorMatches {actualError ->
                    actualError instanceof ApiClientErrorException &&
                            actualError.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR &&
                            actualError.getErrorTypeEnum() == ErrorTypeEnum.API_SERVER_ERROR &&
                            actualError.getMessage() == "An unexpected error occurred in getTrendingOfCoinsAPI"
                }
                .verify()
    }

}