package main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Data @Entity @ToString(exclude = {"posts", "comments", "votes"})
public class Users {

  @Id @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; //id пользователя

  @Column(nullable = false, columnDefinition = "TINYINT")
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


  @OneToMany(
      mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @OneToMany(
      mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<PostComments> comments = new ArrayList<>();

  @OneToMany(
      mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private  List<PostVotes> votes = new ArrayList<>();
}
