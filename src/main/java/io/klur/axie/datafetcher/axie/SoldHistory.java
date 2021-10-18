package io.klur.axie.datafetcher.axie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("SOLD_HISTORY")
public class SoldHistory {

  private String id;
  private String name;
  @JsonProperty("class")
  private String clazz;
  private int breedCount;

}
