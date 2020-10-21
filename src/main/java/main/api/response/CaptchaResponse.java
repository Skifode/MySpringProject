package main.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class CaptchaResponse {
  private String secret;
  private String image;

}
