package io.klur.axie.datafetcher.axie.repository;

import io.klur.axie.datafetcher.axie.repository.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, String> {

}
