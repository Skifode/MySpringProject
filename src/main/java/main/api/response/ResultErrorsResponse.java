package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder @JsonInclude(Include.NON_NULL)
public class ResultErrorsResponse {

  private final String successAnswer;
  private final Integer id;
  private final boolean result;
  private final Map<String, String> errors;
}
