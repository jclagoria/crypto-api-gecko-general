package ar.com.api.general.services;

import ar.com.api.general.configuration.ExternalServerConfig;
import ar.com.api.general.model.Ping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus extends CoinGeckoServiceApi {

 private ExternalServerConfig externalServerConfig;
 private WebClient webClient;

 public CoinGeckoServiceStatus(WebClient webClient, ExternalServerConfig externalServerConfig) {
  this.webClient = webClient;
  this.externalServerConfig = externalServerConfig;
 }

 public Mono<Ping> getStatusCoinGeckoService() {
  
  log.info("Calling method -> " + externalServerConfig.getPing());

  return webClient
         .get()
         .uri(externalServerConfig.getPing())
         .retrieve()
          .onStatus(
                  status -> status.is4xxClientError(),
                  getClientResponseMonoDataException()
          )
          .onStatus(
                  status -> status.is5xxServerError(),
                  getClientResponseMonoDataException()
          )
         .bodyToMono(Ping.class)
          .doOnError(
                  ManageExceptionCoinGeckoServiceApi::throwServiceException
          );
 }

}
