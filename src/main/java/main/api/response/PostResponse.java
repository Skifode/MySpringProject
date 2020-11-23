package main.api.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import main.model.Post;

@Data
public class PostResponse {
  private int id;
  private long timestamp;
  private Map<String, Object> user = new HashMap<>();
  private String title;
  private String announce;
  private long likeCount;
  private long dislikeCount;
  private long commentCount;
  private long viewCount;

  public PostResponse(Post post) {
    this.setId(post.getId());
    this.setTimestamp(post.getTime()
        .toInstant()
        .getEpochSecond());
    this.setTitle(post.getTitle());
    this.setAnnounce(post.getText().substring(0, 50) + "...");
    this.getUser().put("id", post.getUser().getId());
    this.getUser().put("name", post.getUser().getName());
    this.setLikeCount(post.getLikesCount());
    this.setDislikeCount(post.getDislikeCount());
    this.setCommentCount(post.getCommentsList().size());
    this.setViewCount(post.getViewCount());
  }
}