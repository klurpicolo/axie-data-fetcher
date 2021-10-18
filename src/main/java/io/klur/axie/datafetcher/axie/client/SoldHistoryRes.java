package io.klur.axie.datafetcher.axie.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SoldHistoryRes {

  private String id;
  private String name;
  @JsonProperty("class")
  private String clazz;
  private int breedCount;

}
