package io.klur.axie.datafetcher.country;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryRawClient {

  private final WebClientGraphQLClient countryClient;

  public void printCountryDetail() {
    var query = "query($code: ID!) {\n" +
				" country(code: $code) {\n" +
				"   name\n" +
				"   capital\n" +
				"   currency\n" +
				" }\n" +
				"}";
		Map<String, ?> variables = Map.of("code", "US");

   	Mono<GraphQLResponse> res = countryClient.reactiveExecuteQuery(query, variables);

    Mono<Map> somefield = res.map(r -> r.extractValue("data"));
    log.info("printCountryDetail : {}", somefield.block().toString());
  }



}
