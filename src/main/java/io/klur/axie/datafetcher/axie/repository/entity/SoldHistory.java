package io.klur.axie.datafetcher.axie.repository.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SoldHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String _id;
  private Instant timestamp;
  @Column(name="axie_id")
  private String axieId;
  private String name;
  @Column(name="class")
  private String clazz;
  private int breedCount;
  private BigDecimal withPrice;
  private BigDecimal withPriceUsd;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    SoldHistory that = (SoldHistory) o;

    return Objects.equals(_id, that._id);
  }

  @Override
  public int hashCode() {
    return 850175680;
  }
}
