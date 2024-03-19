package ar.com.api.general.exception;

import ar.com.api.general.enums.ErrorTypeEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiClientErrorException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ErrorTypeEnum errorTypeEnum;

    public ApiClientErrorException(String message, ErrorTypeEnum typeEnum, HttpStatus status) {
        super(message);
        this.errorTypeEnum = typeEnum;
        this.httpStatus = status;
    }

}
