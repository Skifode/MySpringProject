package main.repositories;

import java.util.Optional;
import main.model.PostVote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostVotesRepository extends CrudRepository<PostVote, Integer> {
  @Query(value = "select * from post_vote where user_id = :user_id and post_id = :post_id"
      , nativeQuery = true)
  Optional<PostVote> findByUserIdAndPostId(
      @Param("user_id") int userId, @Param("post_id") int postId);

}
