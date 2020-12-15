package main.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponse {

  private final int id;
  private final String name;
  private final String photo;
  private final String email;
  private final boolean moderation;
  private final int moderationCount;
  private final boolean settings;
}
