package main.repository;

import java.util.List;
import main.model.Tag2Post;
import org.springframework.data.repository.CrudRepository;

public interface Tag2PostRepository extends CrudRepository<Tag2Post, Integer> {

  List<Tag2Post> findByPostId(int id);

  List<Tag2Post> findByTagId(int id);
}

