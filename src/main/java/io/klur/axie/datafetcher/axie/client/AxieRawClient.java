package io.klur.axie.datafetcher.axie.client;

import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.WebClientGraphQLClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AxieRawClient {

  private final WebClientGraphQLClient axieClient;

  public List<SoldHistoryRes> getRecentlySold() {
    return getRecentlySold(0, 1);
  }

  public List<SoldHistoryRes> getRecentlySold(int from, int size) {
    var query = "query GetRecentlyAxiesSold($from: Int, $size: Int) {\n" +
      "  settledAuctions {\n" +
      "    axies(from: $from, size: $size) {\n" +
      "      total\n" +
      "      results {\n" +
      "          ...AxieSettledBrief\n" +
      "          transferHistory {\n" +
      "          ...TransferHistoryInSettledAuction\n" +
      "        }\n" +
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
      "}\n" +
      "\n" +
      "fragment TransferHistoryInSettledAuction on TransferRecords {\n" +
      "  total\n" +
      "  results {\n" +
      "    ...TransferRecordInSettledAuction\n" +
      "  }\n" +
      "}\n" +
      "\n" +
      "fragment TransferRecordInSettledAuction on TransferRecord {\n" +
      "  timestamp\n" +
      "  withPrice\n" +
      "  withPriceUsd\n" +
      "}";
    Map<String, ?> variables = Map.of("from", from, "size", size);

    Mono<GraphQLResponse> res = axieClient.reactiveExecuteQuery(query, variables)
        .retry(3)
        .doOnError(ex -> log.error(ex.getMessage()));

    return new ArrayList<>(Objects.requireNonNull(res
        .map(r -> r.extractValueAsObject("data.settledAuctions.axies.results", SoldHistoryRes[].class))
        .map(Arrays::asList)
        .block()));
  }

}
