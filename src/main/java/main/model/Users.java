package main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data @Entity
public class Users {

  @Id @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id; //id пользователя

  @Column(nullable = false)
  private boolean isModerator; //является ли пользователь модератором
              // (может ли правитьглобальные настройки сайта и модерировать посты)

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Setter(AccessLevel.NONE)
  @Temporal(TemporalType.DATE)
  private Date regTime; //дата и время регистрации пользователя

  @Column(nullable = false)
  private String name; //имя пользователя

  @Column(nullable = false)
  private String email; //e-mail пользователя

  @Column(nullable = false)
  private int password; //хэш пароля пользователя

  @Column(nullable = true)
  private int code; //код для восстановления пароля, может быть NULL

  @Column(columnDefinition = "TEXT", nullable = true)
  private String photo; //фотография (ссылка на файл), может быть NULL
}
