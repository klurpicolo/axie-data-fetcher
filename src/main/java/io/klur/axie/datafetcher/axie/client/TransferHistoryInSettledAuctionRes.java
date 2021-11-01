package io.klur.axie.datafetcher.axie.client;

import lombok.Data;

import java.util.List;

@Data
public class TransferHistoryInSettledAuctionRes {

  private int total;
  private List<TransferRecordInSettledAuctionRes> results;

}
