package io.klur.axie.datafetcher.country;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import io.klur.axie.datafetcher.generated.client.CountryGraphQLQuery;
import io.klur.axie.datafetcher.generated.client.CountryProjectionRoot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryDGSClient {

  private final WebClientGraphQLClient countryClient;

  public void printCountryDetail() {
    var req = new GraphQLQueryRequest(
        CountryGraphQLQuery.newRequest()
            .code("TH")
            .build(),
        new CountryProjectionRoot()
            .code()
            .currency()
    );
    var query  = req.serialize();

    Mono<GraphQLResponse> res = countryClient.reactiveExecuteQuery(query);


    Mono<Map> somefield = res.map(r -> r.extractValue("data"));
    log.info("printCountryDetail : {}", somefield.block().toString());
  }



}
