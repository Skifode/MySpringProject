package main.repositories;

import java.util.List;
import main.model.PostComments;
import org.springframework.data.repository.CrudRepository;

public interface PostCommentsRepository extends CrudRepository<PostComments, Integer> {

  List<PostComments> getByPostId(int id);
}
