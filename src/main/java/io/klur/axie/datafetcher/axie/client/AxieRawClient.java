package io.klur.axie.datafetcher.axie.client;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AxieRawClient {

  private final WebClientGraphQLClient axieClient;

  public List<SoldHistoryRes> getRecentlySold() {
    var query = "query GetRecentlyAxiesSold($from: Int, $size: Int) {\n" +
        "  settledAuctions {\n" +
        "    axies(from: $from, size: $size) {\n" +
        "      total\n" +
        "      results {\n" +
        "          ...AxieSettledBrief\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}\n" +
        "\n" +
        "fragment AxieSettledBrief on Axie {\n" +
        "  id\n" +
        "  name\n" +
        "  class\n" +
        "  breedCount\n" +
        "  parts {\n" +
        "    name\n" +
        "    class\n" +
        "    type\n" +
        "  }\n" +
        "}";
    Map<String, ?> variables = Map.of("from", 0, "size", 10);

    Mono<GraphQLResponse> res = axieClient.reactiveExecuteQuery(query, variables);
    return res.map(r -> r.extractValueAsObject("data.settledAuctions.axies.results", SoldHistoryRes[].class)).map(Arrays::asList).block();

  }

}
