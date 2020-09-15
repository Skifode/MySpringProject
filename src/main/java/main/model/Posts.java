package main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data @Entity
public class Posts {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id; //id поста

  @Column(columnDefinition = "TINYINT(1) DEFAULT 1", nullable = false)
  private boolean isActive; //скрыта или активна публикация: 0 или 1

  @Column(columnDefinition = "VARCHAR(32) DEFAULT 'NEW'", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Status moderationStatus = Status.NEW; //статус модерации, по умолчанию значение "NEW".

  @Column(nullable = true)
  private int moderatorId; //ID пользователя-модератора, принявшего решение, или NULL

  @Column(nullable = false)
  private int userID; //автор поста

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Temporal(TemporalType.DATE)
  private Date time; //дата и время публикации поста

  @Column(nullable = false)
  private String title; //заголовок поста

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text; //текст поста

  @Column(nullable = false)
  private int viewCount; //количество просмотров поста
}

enum Status {
      NEW,
      ACCEPTED,
      DECLINED
}
