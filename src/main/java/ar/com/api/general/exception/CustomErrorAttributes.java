package ar.com.api.general.exception;

import ar.com.api.general.exception.external.CoinGeckoBadRequestException;
import ar.com.api.general.exception.external.CoinGeckoServerException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Optional;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Optional<HttpStatus> errorStatus = determineHttpStatus(getError(webRequest));

        errorStatus.ifPresent(
                httpStatus -> {
                    errorAttributes.replace("status", httpStatus.value());
                    errorAttributes.replace("error", httpStatus.getReasonPhrase());
                }
        );

        return errorAttributes;
    }

    @Override
    protected String getMessage(WebRequest webRequest, Throwable error) {
        return super.getMessage(webRequest, error);
    }

    private Optional<HttpStatus> determineHttpStatus(Throwable error) {

        if(error instanceof CoinGeckoBadRequestException) {
            return Optional.of(HttpStatus.NOT_FOUND);
        }

        if(error instanceof CoinGeckoServerException){
            return Optional.of(HttpStatus.SERVICE_UNAVAILABLE);
        }

        return Optional.empty();

    }

}
