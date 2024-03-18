package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrendingCategories {

    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    @JsonProperty("market_cap_1h_change")
    private double marketCap1hChange;

    @JsonProperty
    private String slug;

    @JsonProperty("coins_count")
    private long coinsCount;

    @JsonProperty
    private CategoryData data;
}
