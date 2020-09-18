package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity @Data
public class GlobalSettings {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id; //id настройки

  @Column(nullable = false, unique = true)
  private String code; //системное имя настройки

  @Column(nullable = false)
  private String value; //название настройки

  @Column(nullable = false)
  private String name; //значение настройки
}
