package main.repository;

import java.util.List;
import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TagsRepository extends CrudRepository<Tag, Integer> {

  Tag findByName(String name);

  boolean existsByName(String name);

  @Query(value =
      "SELECT * FROM tag where id in "
          + "("
          + "SELECT distinct tag_id as id FROM tag2post where post_id = "
            + "("
            + "select id from post where id = post_id"
            + " and is_active = 1"
            + " and moderation_status = 'ACCEPTED'"
            + ")"
          + ")", nativeQuery = true)
  List<Tag> findAllAcceptedTags();

  @Query(value = "SELECT name FROM tag where id in "
      + "(select tag_id from tag2post where post_id = :post_id)", nativeQuery = true)
  List<String> findTagNamesByPostId(@Param("post_id") int postId);
}
