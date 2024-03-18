package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrendingNfts {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String symbol;

    @JsonProperty
    private String thumb;

    @JsonProperty("nft_contract_id")
    private long nftContractId;

    @JsonProperty("native_currency_symbol")
    private String nativeCurrencySymbol;

    @JsonProperty("floor_price_in_native_currency")
    private double floorPriceInNativeCurrency;

    @JsonProperty("floor_price_24h_percentage_change")
    private double floorPrice24hPercentageChange;

    @JsonProperty
    private NftsData data;

}
