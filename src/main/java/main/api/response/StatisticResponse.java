package main.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatisticResponse {

  private final int likesCount;
  private final int dislikesCount;
  private final int postsCount;
  private final int viewsCount;
  private final long firstPublication;
}
