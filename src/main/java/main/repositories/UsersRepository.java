package main.repositories;

import java.util.List;
import main.model.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Integer> {
  Users findByEmail(String email);
  Users findByName(String name);
  List<Users> findAll();
}
