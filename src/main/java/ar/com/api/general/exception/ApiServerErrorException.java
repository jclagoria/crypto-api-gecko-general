package ar.com.api.general.exception;

import ar.com.api.general.enums.ErrorTypeEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiServerErrorException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String originalMessage;
    private final ErrorTypeEnum errorTypeEnum;

    public ApiServerErrorException(String message, String originalMessage, ErrorTypeEnum typeEnum, HttpStatus status) {
        super(message);
        this.originalMessage = originalMessage;
        this.errorTypeEnum = typeEnum;
        this.httpStatus = status;
    }

}
