package io.klur.axie.datafetcher.axie;

import io.klur.axie.datafetcher.axie.client.AxieRawClient;
import io.klur.axie.datafetcher.axie.client.PartRes;
import io.klur.axie.datafetcher.axie.client.PartType;
import io.klur.axie.datafetcher.axie.client.SoldHistoryRes;
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

  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
  public void pullSoldHistoryData() {
    log.debug("pullSoldHistoryData at : {}", Instant.now().toString());
    var records = axieClient.getRecentlySold();
    log.debug("receive size : {}", records.size());

    records.forEach(e -> log.debug("record : {}", e.toString()));
    soldHistoryRepository.saveAll(records.stream().map(this::toRecord).collect(Collectors.toList()));

    var total = soldHistoryRepository.findAll().size();
    log.debug("current data : {}", total);
  }

  SoldHistory toRecord(SoldHistoryRes res) {
    PartRes eyes = null;
    PartRes ears = null;
    PartRes back = null;
    PartRes mouth = null;
    PartRes horn = null;
    PartRes tail = null;
    for( PartRes part : res.getParts()) {
      if (part.getType() == PartType.Eyes) {
        eyes = part;
      }
      if (part.getType() == PartType.Ears) {
        ears = part;
      }
      if (part.getType() == PartType.Back) {
        back = part;
      }
      if (part.getType() == PartType.Mouth) {
        mouth = part;
      }
      if (part.getType() == PartType.Horn) {
        horn = part;
      }
      if (part.getType() == PartType.Tail) {
        tail = part;
      }
    }

    return SoldHistory.builder()
        .id(res.getId())
        .name(res.getName())
        .clazz(res.getClazz())
        .breedCount(res.getBreedCount())
        .eyesName(eyes.getName())
        .eyesClazz(eyes.getClazz())
        .earsName(ears.getName())
        .earsClazz(ears.getClazz())
        .backName(back.getName())
        .backClazz(back.getClazz())
        .mouthName(mouth.getName())
        .mouthClazz(mouth.getClazz())
        .hornName(horn.getName())
        .hornClazz(horn.getClazz())
        .tailName(tail.getName())
        .tailClazz(tail.getClazz())
        .build();
  }

}
