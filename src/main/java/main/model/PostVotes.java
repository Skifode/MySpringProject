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

@Data
@Entity
public class PostVotes {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id; //id лайка/дизлайка

  @Column(nullable = false)
  private int userId; //тот, кто поставил лайк / дизлайк

  @Column(nullable = false)
  private int postId; //пост, которому поставлен лайк / дизлайк

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Temporal(TemporalType.DATE)
  private Date time; //дата и время лайка / дизлайка

  @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
  private boolean value; //лайк или дизлайк: 1 или -1

}
