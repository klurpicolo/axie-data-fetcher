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
    var batchNumber = 0;
    while(toFetchMore && batchNumber < 10) {
      var records = axieClient.getRecentlySold(batchNumber, 20);
      log.info("Fetch batch {}", batchNumber);

      var recordIdx = 0;
      for (var record : records) {
        recordIdx++;
        //duplicated in db or already exists in totalRecords
        if(soldHistoryRepository.findByAxieId(record.getId()).isPresent()) {
          log.info("Stop on records since duplicated with db: {}", record);
          toFetchMore = false;
          break;
        } else if(totalRecords.stream().anyMatch(r -> r.equals(record))) {
          log.info("Record duplicated {}/{} with current fetch: {}", recordIdx, records.size(), record);
        } else {
          totalRecords.add(record);
        }
      }
      batchNumber++;
    }

    log.info("Total size {}", totalRecords.size());
    totalRecords.forEach(this::saveSoldHistoryAndPart);
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
    return SoldHistory.builder()
      .axieId(res.getId())
      .name(res.getName())
      .clazz(res.getClazz())
      .breedCount(res.getBreedCount())
      .build();
  }

}
