package main.api.response;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class RegisterResponse {

  private boolean result;
  private Map<String, String> errors;
}
