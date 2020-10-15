package main.api.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class TagResponse {

  private String name;
  private double weight;

}
