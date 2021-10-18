package io.klur.axie.datafetcher.axie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullDataScheduler {

  private final AxieRawClient axieClient;
  private final SoldHistoryRepository soldHistoryRepository;

  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
  public void pullSoldHistoryData() {
    log.info("pullSoldHistoryData at : {}", Instant.now().toString());
    var records = axieClient.getRecentlySold().block();
    log.info("receive size : {}", records.size());

    records.forEach(e -> log.info("record : {}", e.toString()));
    soldHistoryRepository.saveAll(records);

    var total = soldHistoryRepository.findAll().count().block();
    log.info("current data : {}", total);

  }

}
