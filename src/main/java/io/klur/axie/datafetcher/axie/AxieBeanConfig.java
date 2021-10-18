package io.klur.axie.datafetcher.axie;

import com.netflix.graphql.dgs.client.MonoGraphQLClient;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AxieBeanConfig {

  @Bean("axieClient")
  WebClientGraphQLClient webClientGraphQLClient() {
    var webClient = WebClient.create("https://graphql-gateway.axieinfinity.com/graphql");
    return MonoGraphQLClient.createWithWebClient(webClient);
//    return FluxGr
  }





}
