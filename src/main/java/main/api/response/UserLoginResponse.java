package main.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponse {

  private int id;
  private String name;
  private String photo;
  private String email;
  private boolean moderation;
  private int moderationCount;
  private boolean settings;
}
