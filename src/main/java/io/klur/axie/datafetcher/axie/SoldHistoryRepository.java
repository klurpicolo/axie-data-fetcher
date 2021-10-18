package io.klur.axie.datafetcher.axie;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldHistoryRepository extends ReactiveCrudRepository<SoldHistory, Long> {

}