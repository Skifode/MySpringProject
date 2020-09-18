package main.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Entity @Data
public class PostComments {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id; //id комментария

  @Column(nullable = true)
  private int parentId; //комментарий, на который оставлен этот комментарий
                        // (можетбыть NULL, если комментарий оставлен просто к посту)


  @Column(nullable = false)
  private int postId; //пост, к которому написан комментарий

  @Column(nullable = false)
  private int userId; //автор комментария

  @Column(nullable = false, columnDefinition = "DATETIME")
  @Temporal(TemporalType.DATE)
  private Date time; //дата и время комментария

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text; //текст комментария
}
