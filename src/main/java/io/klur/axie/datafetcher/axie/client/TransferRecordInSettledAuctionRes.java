package io.klur.axie.datafetcher.axie.client;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRecordInSettledAuctionRes {

  private long timestamp;
  private BigDecimal withPrice;
  private BigDecimal withPriceUsd;

}
