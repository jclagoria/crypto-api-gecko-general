package ar.com.api.general.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.Optional;

@Builder
public class SimplePriceFilterDTO implements IFilter, Serializable {

    private String ids;
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
                    .append(includeMarketCap.isPresent() ? includeMarketCap.get() : "false")
                .append("&include_24hr_vol=")
                    .append(include24hrVol.isPresent() ? include24hrVol.get() : "false")
                .append("&include_24hr_change=")
                    .append(include24hrChange.isPresent() ? include24hrChange.get() : "false")
                .append("&=include_last_updated_at")
                    .append(includeLastUpdatedAt.isPresent() ? includeLastUpdatedAt.get() : "false")
                .append("&precision=")
                    .append(precision.isPresent() ? precision.get() : "0");

        return strFilter.toString();
    }
}