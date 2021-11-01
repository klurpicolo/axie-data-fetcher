package io.klur.axie.datafetcher.axie;

import io.klur.axie.datafetcher.axie.client.AxieRawClient;
import io.klur.axie.datafetcher.axie.client.PartRes;
import io.klur.axie.datafetcher.axie.client.SoldHistoryRes;
import io.klur.axie.datafetcher.axie.repository.PartRepository;
import io.klur.axie.datafetcher.axie.repository.SoldHistoryRepository;
import io.klur.axie.datafetcher.axie.repository.entity.Part;
import io.klur.axie.datafetcher.axie.repository.entity.SoldHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullDataScheduler {

  private final AxieRawClient axieClient;
  private final SoldHistoryRepository soldHistoryRepository;
  private final PartRepository partRepository;

  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
  public void pullSoldHistoryData() {
    log.info("pullSoldHistoryData at : {}", Instant.now().toString());
    var totalRecords = new ArrayList<SoldHistoryRes>();
    var toFetchMore = true;
    int batchPullTimes = 10;
    for(var batchNumber = 0; batchNumber< batchPullTimes && toFetchMore; batchNumber++ ) {
      var records = axieClient.getRecentlySold(batchNumber, 20);
      log.info("Fetch batch {}", batchNumber);
      var recordIdx = 0;
      for (var record : records) {
        recordIdx++;
        //duplicated in db or already exists in totalRecords
        if(soldHistoryRepository.findByAxieId(record.getId()).isPresent()) {
          log.error("Stop on records since duplicated with db: {}", record);
          toFetchMore = false;
          break;
        } else if(totalRecords.stream().anyMatch(r -> r.equals(record))) {
          log.warn("Record duplicated {}/{} with current fetch: {}", recordIdx, records.size(), record);
        } else {
          totalRecords.add(record);
        }
      }
    }
    var distinct = totalRecords.stream().distinct().collect(Collectors.toList());

    log.info("Total size {}", distinct.size());
    distinct.forEach(this::saveSoldHistoryAndPart);
  }

  private void saveSoldHistoryAndPart(SoldHistoryRes res) {
    var savedSoldHistory = soldHistoryRepository.save(toRecord(res));
    partRepository.saveAll(res.getParts().stream().map(partRes -> toRecord(partRes, savedSoldHistory)).collect(Collectors.toList()));
  }

  private Part toRecord(PartRes res, SoldHistory soldHistory) {
    return Part.builder()
      .soldHistory(soldHistory)
      .name(res.getName())
      .clazz(res.getClazz())
      .type(res.getType())
      .build();
  }

  private SoldHistory toRecord(SoldHistoryRes res) {
    var lastTransferHistoryRes = res.getTransferHistory().getResults().get(0);
    return SoldHistory.builder()
      .axieId(res.getId())
      .timestamp(Instant.ofEpochSecond(lastTransferHistoryRes.getTimestamp()))
      .name(res.getName())
      .clazz(res.getClazz())
      .breedCount(res.getBreedCount())
      .withPrice(lastTransferHistoryRes.getWithPrice())
      .withPriceUsd(lastTransferHistoryRes.getWithPriceUsd())
      .build();
  }

}
