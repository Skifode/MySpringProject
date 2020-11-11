package main.api.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@AllArgsConstructor
@NoArgsConstructor
public class AddPostRequest {

  private Date timestamp;
  private boolean isActive;
  private String title;
  private List<String> tags;
  private String text;
}
