package main.repositories;

import java.util.Date;
import java.util.List;
import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostsRepository extends CrudRepository<Post, Integer> {

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status  = 'ACCEPTED' and date(time) = :date order by -time", nativeQuery = true)
  List<Post> findByDate(Pageable page, @Param(value = "date") Date date);

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status  = 'ACCEPTED' order by -time", nativeQuery = true)
  List<Post> findByRecent(Pageable page);

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status  = 'ACCEPTED' order by time", nativeQuery = true)
  List<Post> findByEarly(Pageable page);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status  = 'ACCEPTED' order by -(select count(*) from post_comment\n"
          + "where post.id = post_comment.post_id)", nativeQuery = true)
  List<Post> findByPopular(Pageable page);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status  = 'ACCEPTED' order by (select sum(value = 1) from post_vote\n"
          + "where post.id = post_vote.post_id) desc", nativeQuery = true)
  List<Post> findByBest(Pageable page);

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status = 'ACCEPTED' and id in "
      + "(select post_id from tag2post where tag_id = :tagId)"
      + "order by time desc", nativeQuery = true)
  List<Post> findByTag(Pageable page, @Param(value = "tagId") int tagId);

  @Query(value = "SELECT year(time) as years2calendar FROM post group by year(time) order by -year(time)"
      , nativeQuery = true)
  List<YearsListForCalendar> getYears();

  @Query(value = "SELECT COUNT(*) FROM post as p where is_active = 1 "
      + "and moderation_status = 'ACCEPTED' and date(p.time) = :date", nativeQuery = true)
  int getCountOfPostsByDate(Date date);


@Query(value = "SELECT time as date, count(time) as count FROM post"
    + " where is_active = 1 and moderation_status = 'ACCEPTED'"
    + " and year(time) = :year"
    + " group by date(time) order by date(time)", nativeQuery = true)
  List<CountsPostsByDate> getCountPostsGroupByDate(@Param(value = "year") int year);

  @Query(value = "select * from post p where is_active = 1 and moderation_status = 'ACCEPTED' "
      + "and p.title like %:query% or p.text like %:query% order by -time", nativeQuery = true)
  List<Post> findPostsByQuery(Pageable pageable, @Param(value = "query") String query);

  @Query(value = "select count(*) from post p where is_active = 1 and moderation_status = 'ACCEPTED' "
      + "and p.title like %:query% or p.text like %:query% order by -time", nativeQuery = true)
  int getCountOfPostsByQuery(String query);

}