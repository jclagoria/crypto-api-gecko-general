package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trending implements Serializable {

    @JsonProperty("coins")
    private List<TrendingCoin> coins;

    @JsonProperty("exchanges")
    private List<Object> exchanges;

    @JsonProperty("nfts")
    private List<Object> nfts;

}
