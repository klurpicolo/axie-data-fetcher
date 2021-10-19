package io.klur.axie.datafetcher.axie;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SoldHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long _id;
  private String id;
  private String name;
  private String clazz;
  private int breedCount;

  private String eyesName;
  private String eyesClazz;
  private String earsName;
  private String earsClazz;
  private String backName;
  private String backClazz;
  private String mouthName;
  private String mouthClazz;
  private String hornName;
  private String hornClazz;
  private String tailName;
  private String tailClazz;


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
