package ar.com.api.general.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.io.Serializable;
import java.util.Optional;

@Builder
public class SimplePriceFilterDTO implements IFilter, Serializable {

    @NotBlank(message = "Id need a value")
    @NotEmpty(message = "Id need a value")
    private String ids;

    @NotBlank(message = "Currency need a value")
    @NotEmpty(message = "Currency need a value")
    private String vsCurrencies;

    private Optional<String> includeMarketCap;
    private Optional<String> include24hrVol;
    private Optional<String> include24hrChange;
    private Optional<String> includeLastUpdatedAt;
    private Optional<String> precision;

    @Override
    public String getUrlFilterString() {

        StringBuilder strFilter = new StringBuilder();

        strFilter.append("?ids=").append(ids)
                .append("&vs_currencies=")
                .append(vsCurrencies)
                .append("&include_market_cap=")
                .append(includeMarketCap.orElse("false"))
                .append("&include_24hr_vol=")
                .append(include24hrVol.orElse("false"))
                .append("&include_24hr_change=")
                .append(include24hrChange.orElse("false"))
                .append("&include_last_updated_at=")
                .append(includeLastUpdatedAt.orElse("false"))
                .append("&precision=")
                .append(precision.orElse("18"));

        return strFilter.toString();
    }
}
