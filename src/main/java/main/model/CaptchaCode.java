package main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @NoArgsConstructor
public class CaptchaCode {
  
  public CaptchaCode(Date time, String code, String secretCode) {
    this.time = time;
    this.code = code;
    this.secretCode = secretCode;
  }

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; //id каптча

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Temporal(TemporalType.DATE)
  private Date time; //дата и время генерации кода капчи

  @Column(columnDefinition = "TINYTEXT", nullable = false)
  private String code; //код, отображаемый на картинкке капчи

  @Column(columnDefinition = "TINYTEXT", nullable = false)
  private String secretCode; //код, передаваемый в параметре
}
