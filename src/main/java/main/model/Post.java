package main.model;

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
import lombok.Data;
import main.data.Status;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Data @Entity
public class Post {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; //id поста

  @Column(columnDefinition = "TINYINT", nullable = false)
  private boolean isActive; //скрыта или активна публикация: 0 или 1

  @Column(columnDefinition = "VARCHAR(32) DEFAULT 'NEW'", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Status moderationStatus = Status.NEW; //статус модерации, по умолчанию значение "NEW".

  private int moderatorId; //ID пользователя-модератора, принявшего решение, или NULL

  @Column(nullable = false, name = "user_id")
  private int userId; //автор поста

  @Column(nullable = false, columnDefinition = "DATETIME")
  //@Setter(AccessLevel.NONE)
  @Temporal(TemporalType.DATE)
  private Date time; //дата и время публикации поста

  @Column(nullable = false)
  private String title; //заголовок поста

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text; //текст поста

  @Column(nullable = false)
  private int viewCount; //количество просмотров поста

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;
  //===================== КОММЕНТЫ ====================

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post",
      orphanRemoval = true, fetch = FetchType.LAZY)
  @LazyCollection(LazyCollectionOption.EXTRA)
  private List<PostComment> commentsList = new ArrayList<>();

  public void addComment(PostComment comment) {
    commentsList.add(comment);
  }

  public void removeComment(PostComment comment) {
    commentsList.remove(comment);
  }
  //======================= ТЭГИ ====================

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "tag2post",
      joinColumns = @JoinColumn(name = "post_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

  public void addTag(Tag tag) {
    tags.add(tag);
  }

  public void removeTag(Tag tag) {
    tags.remove(tag);
  }

  //====================== ЛАЙКИ ====================

  @OneToMany(
      mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private  List<PostVote> votes = new ArrayList<>();

  public long getLikesCount() {
    return votes.stream().filter(vote -> vote.getValue() > 0).count();
  }

  public long getDislikeCount() {
    return votes.stream().filter(vote -> vote.getValue() < 0).count();
  }
  //==================================================
  public void incrementView() {
    setViewCount(getViewCount()+1);
  }
}
