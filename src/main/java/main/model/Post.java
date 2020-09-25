package main.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import main.data.Status;

@Data @Entity
public class Post {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; //id поста

  @Column(columnDefinition = "TINYINT DEFAULT 1", nullable = false)
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
  //===================== КОММЕНТЫ ====================

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post",
      orphanRemoval = true, fetch = FetchType.LAZY)

  private List<PostComments> commentsList = new ArrayList<>();

  public void addComment(PostComments comment) {
    commentsList.add(comment);
    comment.setPostId(this.getId());
  }

  public void removeComment(PostComments comment) {
    commentsList.remove(comment);
    comment.setPost(null);
  }
  //======================= ТЭГИ ====================

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "tag2post",
      joinColumns = @JoinColumn(name = "post_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tags> tags = new HashSet<>();

  public void addTag(Tags tag) {
    tags.add(tag);
    tag.getPosts().add(this);
  }

  public void removeTag(Tags tag) {
    tags.remove(tag);
    tag.getPosts().remove(this);
  }

  //====================== ЛАЙКИ ====================

  @OneToMany(
      mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private  List<PostVotes> votes = new ArrayList<>();

  public long getLikesCount() {
    return votes.stream().filter(PostVotes::isValue).count();
  }

  public long getDislikeCount() {
    return votes.size()-votes.stream().filter(PostVotes::isValue).count();
  }
}
