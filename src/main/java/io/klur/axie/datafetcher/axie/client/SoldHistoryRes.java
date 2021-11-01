package io.klur.axie.datafetcher.axie.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SoldHistoryRes {

  private String id;
  private String name;
  @JsonProperty("class")
  private String clazz;
  private int breedCount;
  private List<PartRes> parts;
  private TransferHistoryInSettledAuctionRes transferHistory;

}
