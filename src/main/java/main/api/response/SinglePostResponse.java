package main.api.response;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SinglePostResponse {

  private final int id;
  private final long timestamp;
  private final boolean active;
  private final Map<String, Object> user;
  private final String title;
  private final String text;
  private final long likeCount;
  private final long dislikeCount;
  private final long viewCount;

  private final List<PostCommentsResponse> comments;
  private final List<String> tags;
}
