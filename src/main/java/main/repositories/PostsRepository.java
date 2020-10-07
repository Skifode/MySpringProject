package main.repositories;

import java.util.List;
import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PostsRepository extends CrudRepository<Post, Integer> {

  @Query(value = "SELECT * FROM post order by -time", nativeQuery = true)
  List<Post> findByRecent(Pageable page);

  @Query(value = "SELECT * FROM post order by time", nativeQuery = true)
  List<Post> findByEarly(Pageable page);

  @Query(value = "SELECT * FROM post order by -(select count(*) from post_comments\n"
      + "where post.id = post_comments.post_id)", nativeQuery = true)
  List<Post> findByPopular(Pageable page);

  @Query(value = "SELECT * FROM post order by (select sum(value = 1) from post_votes\n"
      + "where post.id = post_votes.post_id) desc", nativeQuery = true)
  List<Post> findByBest(Pageable page);

}