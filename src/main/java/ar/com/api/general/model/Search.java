package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Search {

    @JsonProperty("coins")
    private List<CoinSearch> coinList;

    @JsonProperty("exchanges")
    private List<Exchange> exhcangeList;

    @JsonProperty("categories")
    private List<Categories> categoriesList;

    @JsonProperty("nfts")
    private List<Nfts> ntfsList;
}
