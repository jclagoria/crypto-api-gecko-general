package ar.com.api.general.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class SearchDTO implements IFilter {

    @NotEmpty(message = "QueryParam need a value")
    private String queryParam;
    @Override
    public String getUrlFilterString() {

        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append("?query=").append(queryParam);

        return urlBuilder.toString();
    }
}
