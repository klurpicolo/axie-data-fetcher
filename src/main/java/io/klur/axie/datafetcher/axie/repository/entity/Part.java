package io.klur.axie.datafetcher.axie.repository.entity;

import io.klur.axie.datafetcher.axie.client.PartType;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Part {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String _id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name="axie_id", referencedColumnName = "id", nullable=false)
  private SoldHistory soldHistory;
  private String name;
  @Column(name = "class")
  private String clazz;
  private PartType type;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Part part = (Part) o;

    return Objects.equals(_id, part._id);
  }

  @Override
  public int hashCode() {
    return 1725270524;
  }
}
