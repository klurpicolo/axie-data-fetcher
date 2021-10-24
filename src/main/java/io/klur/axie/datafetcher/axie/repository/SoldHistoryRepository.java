package io.klur.axie.datafetcher.axie.repository;

import io.klur.axie.datafetcher.axie.repository.entity.SoldHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoldHistoryRepository extends JpaRepository<SoldHistory, String> {

  Optional<SoldHistory> findByAxieId(String axieId);

}