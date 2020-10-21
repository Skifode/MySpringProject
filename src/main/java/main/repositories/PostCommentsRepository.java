package main.repositories;

import java.util.List;
import main.model.PostComment;
import org.springframework.data.repository.CrudRepository;

public interface PostCommentsRepository extends CrudRepository<PostComment, Integer> {

  List<PostComment> getByPostId(int id);
}
