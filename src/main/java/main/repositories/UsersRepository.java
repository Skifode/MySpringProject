package main.repositories;

import java.util.List;
import main.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Integer> {
  User findByEmail(String email);
  User findByName(String name);
  boolean existsByEmail(String email);
  List<User> findAll();
}
