package ar.com.api.general.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiCustomException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApiCustomException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

}
