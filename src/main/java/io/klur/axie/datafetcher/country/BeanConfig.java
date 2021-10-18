package io.klur.axie.datafetcher.country;

import com.netflix.graphql.dgs.client.MonoGraphQLClient;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {

  @Bean("countryClient")
  WebClientGraphQLClient webClientGraphQLClient() {
    var webClient = WebClient.create("https://countries.trevorblades.com/");
    return MonoGraphQLClient.createWithWebClient(webClient);
  }


}
