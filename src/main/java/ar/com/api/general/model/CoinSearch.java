package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinSearch {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("api_symbol")
    private String apiSymbol;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("market_cap_rank")
    private long marketCapRank;

    @JsonProperty("thumb")
    private String thumb;

    @JsonProperty("large")
    private String large;

}
