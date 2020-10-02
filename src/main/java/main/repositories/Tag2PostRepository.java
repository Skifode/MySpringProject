package main.repositories;

import java.util.List;
import main.model.Tag2Post;
import org.springframework.data.repository.CrudRepository;

public interface Tag2PostRepository extends CrudRepository<Tag2Post, Integer> {

  List<Tag2Post> getByPostId(int id);

  List<Tag2Post> getByTagId(int id);
}
