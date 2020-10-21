package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
@JsonInclude(Include.NON_NULL)
public class LoginResponse {

  private final boolean result;
  @JsonProperty("user")
  private final UserLoginResponse userLoginResponse;
}
