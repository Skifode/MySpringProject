package main.api.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class SinglePostResponse {
  public SinglePostResponse(PostResponse obj, boolean isActive, String text) {
    this.id = obj.getId();
    this.timestamp = obj.getTimestamp();
    this.active = isActive; //ну, тут временно
    this.user = obj.getUser();
    this.title = obj.getTitle();
    this.text = text;
    this.likeCount = obj.getLikeCount();
    this.dislikeCount = obj.getDislikeCount();
    this.viewCount = obj.getViewCount();
  }

  private int id;
  private long timestamp;
  private boolean active;
  private Map<String, Object> user = new HashMap<>();
  private String title;
  private String text;
  private long likeCount;
  private long dislikeCount;
  private long viewCount;

  private List<PostCommentsResponse> comments;
  private List<String> tags;
}
