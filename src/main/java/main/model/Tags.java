package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Tags {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id; //id тэга

  @Column(nullable = false)
  private String name; //текст тэга
}
