package io.klur.axie.datafetcher.axie;

import io.klur.axie.datafetcher.axie.client.AxieRawClient;
import io.klur.axie.datafetcher.axie.client.PartRes;
import io.klur.axie.datafetcher.axie.client.SoldHistoryRes;
import io.klur.axie.datafetcher.axie.repository.PartRepository;
import io.klur.axie.datafetcher.axie.repository.entity.Part;
import io.klur.axie.datafetcher.axie.repository.entity.SoldHistory;
import io.klur.axie.datafetcher.axie.repository.SoldHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
    log.debug("pullSoldHistoryData at : {}", Instant.now().toString());
    var records = axieClient.getRecentlySold();
    log.debug("receive size : {}", records.size());

    records.forEach(e -> log.debug("record : {}", e.toString()));
    records.forEach(this::saveSoldHistoryAndPart);

    var total = soldHistoryRepository.findAll().size();
    log.debug("current data : {}", total);
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
        .id(res.getId())
        .name(res.getName())
        .clazz(res.getClazz())
        .breedCount(res.getBreedCount())
        .build();
  }



}
