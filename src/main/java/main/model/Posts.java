package main.model;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import main.enums.Status;

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

  @Column(nullable = false, name = "user_id", insertable = false, updatable = false)
  private int userId; //автор поста

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Setter(AccessLevel.NONE)
  @Temporal(TemporalType.DATE)
  private Date time; //дата и время публикации поста

  @Column(nullable = false)
  private String title; //заголовок поста

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text; //текст поста

  @Column(nullable = false)
  private int viewCount; //количество просмотров поста

  public void setDate(String stringDate) {
    this.time = Date.from(Timestamp.valueOf(stringDate).toInstant());
  }

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Users user;
}
