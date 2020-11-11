package main.api.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentRequest {

  @JsonAlias("parent_id")
  private int parentId;
  @JsonAlias("post_id")
  private int postId;
  private String text;
}
