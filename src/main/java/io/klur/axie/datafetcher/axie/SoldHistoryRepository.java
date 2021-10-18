package io.klur.axie.datafetcher.axie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldHistoryRepository extends JpaRepository<SoldHistory, Long> {

}