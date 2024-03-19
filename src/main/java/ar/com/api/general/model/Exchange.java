package ar.com.api.general.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Exchange {

    @JsonProperty("id")
    private String idExchange;

    @JsonProperty("name")
    private String name;

    @JsonProperty("market_type")
    private String marketType;

    @JsonProperty("thumb")
    private String thumb;

    @JsonProperty("large")
    private String large;
}
