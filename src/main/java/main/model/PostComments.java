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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Entity @Data
public class PostComments {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; //id комментария

  @Column(nullable = true, name = "parent_id", insertable=false, updatable=false)
  private Integer parentId; //комментарий, на который оставлен этот комментарий
                        // (можетбыть NULL, если комментарий оставлен просто к посту)


  @Column(nullable = false, name = "post_id", insertable=false, updatable=false)
  private int postId; //пост, к которому написан комментарий

  @Column(nullable = false, name = "user_id", insertable=false, updatable=false)
  private int userId; //автор комментария

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Temporal(TemporalType.DATE)
  private Date time; //дата и время комментария

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text; //текст комментария

  //==================== К ПОСТУ =====================

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  //================ ПОЛЬЗОВАТЕЛЕМ ====================

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Users user;

  //============== КОММЕНТЫ НА КОММЕНТЫ ==============

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "parent_id", nullable = true)
  private List<PostComments> comments2commentList = new ArrayList<>();

  public void addComment(PostComments comment) {
    comments2commentList.add(comment);
    comment.setParentId(this.getParentId());
  }

  public void removeComment(PostComments comment) {
    comments2commentList.remove(comment);
  }
}
