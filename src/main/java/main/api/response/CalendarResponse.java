package main.api.response;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarResponse {
  List<Integer> years;
  Map<String, Integer> posts;
}
