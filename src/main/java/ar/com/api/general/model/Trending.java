package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trending implements Serializable {

    @JsonProperty
    private List<TrendingCoin> coins;

    @JsonProperty
    private List<TrendingNfts> nfts;

    @JsonProperty
    private List<TrendingCategories> categories;


}
