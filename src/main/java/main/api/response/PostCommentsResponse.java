package main.api.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.PostComment;

@Data
@NoArgsConstructor
public class PostCommentsResponse {

  private int id;
  private long timestamp;
  private String text;
  private Map<String, Object> user = new HashMap<>();

  public PostCommentsResponse(PostComment comment) {
    this.id = comment.getId();
    this.timestamp = comment
        .getTime()
        .toInstant()
        .getEpochSecond();
    this.text = comment.getText();
    user.put("id", comment.getUser().getId());
    user.put("name", comment.getUser().getName());
    user.put("photo", comment.getUser().getPhoto());
  }
}
