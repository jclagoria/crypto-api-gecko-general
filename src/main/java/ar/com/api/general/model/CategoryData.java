package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryData {

    @JsonProperty("market_cap")
    private double marketCap;

    @JsonProperty("market_cap_btc")
    private double marketCapBTC;

    @JsonProperty("total_volume")
    private double totalVolume;

    @JsonProperty("total_volume_btc")
    private double totalVolumeBTC;

    @JsonProperty("market_cap_change_percentage_24h")
    private Map<String, Double> marketCapChangePercentage24h;

    @JsonProperty
    private String sparkline;

}
