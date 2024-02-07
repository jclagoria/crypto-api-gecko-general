package ar.com.api.general.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ExternalServerConfig {

    private String urlCoinGecko;
    private String ping;
    private String global;
    private String decentralized;
    private String search;
    private String searchTrending;
    private String exchangeRates;
    private String simplePrice;
    private String simpleTokePriceById;
}
