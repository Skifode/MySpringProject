package main.data;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor @Getter
public enum Role {
  MODERATOR(Set.of(Permission.MODERATE, Permission.USER)),
  USER(Set.of(Permission.USER));

  private final Set<Permission> permissions;

  public Set<SimpleGrantedAuthority> getAuthorities() {
    return permissions.stream().map(p -> new SimpleGrantedAuthority(p.getPermission()))
        .collect(Collectors.toSet());
  }
}
