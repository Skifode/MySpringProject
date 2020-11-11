package main.repositories;

import java.util.List;
import main.model.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<Tag, Integer> {

  Tag findByName(String name);
  boolean existsByName(String name);
  List<Tag> findAll();
}
