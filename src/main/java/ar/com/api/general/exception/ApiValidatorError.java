package ar.com.api.general.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiValidatorError extends RuntimeException {

    private final HttpStatus httpStatus;
    private String errorMessage;

    public ApiValidatorError(String message, String errorMessage, HttpStatus status) {
        super(message);
        this.errorMessage = errorMessage;
        this.httpStatus = status;
    }

}
