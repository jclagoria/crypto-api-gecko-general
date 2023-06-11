package ar.com.api.general.services;

import ar.com.api.general.exception.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class CoinGeckoServiceApi {
    @NotNull
    static Function<ClientResponse, Mono<? extends Throwable>> getClientResponseMonoFunction() {
        return clientResponse ->
        {
            throw new BadRequestException
                    (
                            clientResponse.statusCode().toString()
                    );
        };
    }
}
