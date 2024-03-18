package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NftsData {

    @JsonProperty("floor_price")
    private String floorPrice;

    @JsonProperty("floor_price_in_usd_24h_percentage_change")
    private String floorPriceInUSD24hPercentageChange;

    @JsonProperty("h24_volume")
    private String h24Volume;

    @JsonProperty("h24_average_sale_price")
    private String h24AverageSalePrice;

    @JsonProperty
    private String sparkline;

    @JsonProperty
    private String content;

}
