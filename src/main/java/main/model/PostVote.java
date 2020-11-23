package main.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class PostVote {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id; //id лайка/дизлайка

  @Column(nullable = false, name = "user_id")
  private int userId; //тот, кто поставил лайк / дизлайк

  @Column(nullable = false, name = "post_id")
  private int postId; //пост, которому поставлен лайк / дизлайк

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Temporal(TemporalType.TIMESTAMP)
  private Date time; //дата и время лайка / дизлайка

  @Column(columnDefinition = "TINYINT", nullable = false)
  private byte value; //лайк или дизлайк: 1 или -1

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable=false, updatable=false)
  private User user;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", insertable=false, updatable=false)
  private Post post;

}
