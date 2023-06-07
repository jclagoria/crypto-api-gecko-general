package ar.com.api.general.dto;

import lombok.Builder;

@Builder
public class SearchDTO implements IFilter {

    private String queryParam;
    @Override
    public String getUrlFilterString() {

        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append("?query=").append(queryParam);

        return urlBuilder.toString();
    }
}
