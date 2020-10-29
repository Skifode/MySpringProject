package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSettingsRequest {

  private Object photo;
  private String name;
  private String email;
  private String password;
  private boolean removePhoto;

}
