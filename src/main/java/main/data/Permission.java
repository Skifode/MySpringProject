package main.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum Permission {

  USER("user:write"),
  MODERATE("user:moderate");

  private final String permission;
}
