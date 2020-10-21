package main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.data.Role;

@Data @Entity @ToString(exclude = {"posts", "comments", "votes"})
@AllArgsConstructor @NoArgsConstructor
public class User { //У тестовых пользователей пароль "password" !!!
  public User(String email, String password, String name) {
    this.isModerator = false;
    this.email = email;
    this.password = password;
    this.name = name;
    this.regTime = new GregorianCalendar().getTime();
    this.photo = "/img/default-1.png";
    this.code = "none";
  }

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

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String password; //хэш пароля пользователя

  @Column(columnDefinition = "VARCHAR(255)")
  private String code; //код для восстановления пароля, может быть NULL

  @Column(columnDefinition = "TEXT")
  private String photo; //фотография (ссылка на файл), может быть NULL

  public Role getRole(){
    return isModerator()? Role.MODERATOR: Role.USER;
  }


  @OneToMany(
      mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @OneToMany(
      mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<PostComment> comments = new ArrayList<>();

  @OneToMany(
      mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private  List<PostVote> votes = new ArrayList<>();
}
