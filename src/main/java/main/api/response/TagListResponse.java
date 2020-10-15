package main.api.response;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class TagListResponse {

  private List<TagResponse> tags;
}
