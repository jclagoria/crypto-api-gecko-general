package ar.com.api.general.exception.external;

public class CoinGeckoServerException extends RuntimeException {
    public CoinGeckoServerException(String message) {
        super(message);
    }

}
