package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCoin {

    @JsonProperty("id")
    private String id;

    @JsonProperty("coin_id")
    private String coinID;

    @JsonProperty("name")
    private String name;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("market_cap_rank")
    private long marketCapRank;

    @JsonProperty("thumb")
    private String thumb;

    @JsonProperty("small")
    private String small;

    @JsonProperty("large")
    private String large;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("price_btc")
    private double priceBTC;

    @JsonProperty("score")
    private long score;

    @JsonProperty("data")
    private ItemData itemData;

}
