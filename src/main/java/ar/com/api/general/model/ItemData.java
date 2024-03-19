package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemData {

    @JsonProperty
    private String price;

    @JsonProperty("price_btc")
    private String priceBtc;

    @JsonProperty("price_change_percentage_24h")
    private Map<String, Double> priceChangePercentage24h;

}
