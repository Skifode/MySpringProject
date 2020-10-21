package main.repositories;

import main.model.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<Tag, Integer> {

  Tag findByName(String name);
}
