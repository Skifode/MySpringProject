package main.repositories;

import java.util.List;
import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<Tag, Integer> {

  Tag findByName(String name);

  boolean existsByName(String name);

  @Query(value =
      "SELECT * FROM tag where id in "
          + "("
          + "SELECT distinct tag_id as id FROM tag2post where post_id = "
            + "("
            + "select id from post where id = post_id and moderation_status = 'ACCEPTED'"
            + ")"
          + ")", nativeQuery = true)
  List<Tag> findAllAccepted();
}
