package io.klur.axie.datafetcher.axie.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PartRes {

  private String name;
  @JsonProperty("class")
  private String clazz;
  private PartType type;

}
