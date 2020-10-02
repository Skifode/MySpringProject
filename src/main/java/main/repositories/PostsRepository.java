package main.repositories;

import java.util.List;
import main.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostsRepository extends CrudRepository<Post, Integer> {

  @Query(value = "SELECT * FROM post order by -time limit :from,:to", nativeQuery = true)
  List<Post> findByRecentIdFromTo(@Param("from") int from, @Param("to") int to);

  @Query(value = "SELECT * FROM post order by time limit :from,:to", nativeQuery = true)
  List<Post> findByEarlyIdFromTo(@Param("from") int from, @Param("to") int to);

  @Query(value = "select * from post order by -(select count(*) from post_comments\n"
      + "where post.id = post_comments.post_id) limit :from,:to", nativeQuery = true)
  List<Post> findByPopularIdFromTo(@Param("from") int from, @Param("to") int to);

  @Query(value = "select * from post order by (select sum(value) from post_votes\n"
      + "where post.id = post_votes.post_id) desc limit :from,:to", nativeQuery = true)
  List<Post> findByBestIdFromTo(@Param("from") int from, @Param("to") int to);

}