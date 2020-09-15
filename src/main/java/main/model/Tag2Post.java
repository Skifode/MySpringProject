package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity @Data
public class Tag2Post {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id; //id связи

  @Column(nullable = false)
  private int postId; //id поста

  @Column(nullable = false)
  private int tagId; //id тэга

}
