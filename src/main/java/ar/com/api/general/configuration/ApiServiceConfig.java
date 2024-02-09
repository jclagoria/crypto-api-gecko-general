package ar.com.api.general.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "coins")
@Getter
@Setter
public class ApiServiceConfig {

    private String baseURL;
    private String global;
    private String decentralized;
    private String search;
    private String searchTrending;
    private String simplePrice;
    private String simpleTokePriceById;
    private String healthAPI;
    private String exchangeRates;

}
