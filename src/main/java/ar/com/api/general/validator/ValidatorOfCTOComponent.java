package ar.com.api.general.validator;

import ar.com.api.general.exception.ApiValidatorError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

public class ValidatorOfCTOComponent {

    private final SpringValidatorAdapter validatorAdapter;

    public ValidatorOfCTOComponent(SpringValidatorAdapter adapter) {
        this.validatorAdapter = adapter;
    }

    public <T> Mono<T> validation(T dto) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, dto.getClass().getName());
        validatorAdapter.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("; "));

            return Mono.error(
                    new ApiValidatorError("Validation failed", errorMessage, HttpStatus.BAD_REQUEST)
            );
        }

        return Mono.just(dto);
    }
}
