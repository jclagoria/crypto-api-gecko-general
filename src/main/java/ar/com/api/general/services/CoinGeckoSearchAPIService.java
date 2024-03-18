package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.configuration.HttpServiceCall;
import ar.com.api.general.dto.SearchDTO;
import ar.com.api.general.model.Search;
import ar.com.api.general.model.Trending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoSearchAPIService {

    private final ExternalServerConfig externalServerConfig;
    private final HttpServiceCall httpServiceCall;

    public CoinGeckoSearchAPIService(HttpServiceCall serviceCall, ExternalServerConfig externalServerConfig) {
        this.httpServiceCall = serviceCall;
        this.externalServerConfig = externalServerConfig;
    }

    public Mono<Search> getSearchFromGeckoApi(SearchDTO filterDTO) {

        log.info("in getSearchFromGeckoApi - Calling Gecko Api Service -> "
                + externalServerConfig.getSearch()
                + filterDTO.getUrlFilterString());

        return httpServiceCall.getMonoObject(externalServerConfig.getSearch()
                + filterDTO.getUrlFilterString(), Search.class);
    }

    public Mono<Trending> getSearchTrendingFromGeckoApi() {
        log.info("in getSearTRendingFromGeckoApi - Calling Gecko Api Service -> "
                + externalServerConfig.getSearchTrending());

        return null;
    }

}
