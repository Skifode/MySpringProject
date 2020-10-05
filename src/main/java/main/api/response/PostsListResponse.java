package main.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostsListResponse {
  private final long count;
  private final List<PostResponse> posts;
}