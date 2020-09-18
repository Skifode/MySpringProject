package main.repositories;

import main.model.PostComments;
import org.springframework.data.repository.CrudRepository;

public interface PostCommentsRepository extends CrudRepository<PostComments, Integer> {

}
