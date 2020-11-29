package main.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostsRepository extends CrudRepository<Post, Integer> {

  @Query(value = "SELECT count(*) FROM post where id = :id and user_id = :user_id"
      , nativeQuery = true)
  int countByUserIdAndPostId(@Param("user_id") int userId, @Param("id") int id);

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status  = 'ACCEPTED' and date(time) = :date "
      + "order by time desc, view_count desc"
      , nativeQuery = true)
  List<Post> findByDate(Pageable page, @Param(value = "date") Date date);

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status  = 'ACCEPTED' order by -time", nativeQuery = true)
  List<Post> findByRecent(Pageable page);

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status  = 'ACCEPTED' order by time", nativeQuery = true)
  List<Post> findByEarly(Pageable page);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status  = 'ACCEPTED' order by (select count(*) from post_comment\n"
          + "where post.id = post_comment.post_id) desc, view_count desc", nativeQuery = true)
  List<Post> findByPopular(Pageable page);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status  = 'ACCEPTED' order by (select sum(value = 1) from post_vote\n"
          + "where post.id = post_vote.post_id) desc, view_count desc", nativeQuery = true)
  List<Post> findByBest(Pageable page);

  @Query(value = "SELECT * FROM post where is_active = 1 "
      + "and moderation_status = 'ACCEPTED' and id in "
      + "(select post_id from tag2post where tag_id = :tagId)"
      + "order by time desc", nativeQuery = true)
  List<Post> findByTag(Pageable page, @Param(value = "tagId") int tagId);

  @Query(value = "SELECT year(time) as years2calendar FROM post "
      + "group by years2calendar"
      + " order by -years2calendar"
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

  @Query(value = "select * from post p where is_active = 1"
      + " and moderation_status = 'ACCEPTED' "
      + "and (p.title like %:query% or p.text like %:query%) order by -time", nativeQuery = true)
  List<Post> findPostsByQuery(Pageable pageable, @Param(value = "query") String query);

  @Query(value = "select count(*) from post p where is_active = 1"
      + " and moderation_status = 'ACCEPTED' "
      + "and p.title like %:query% or p.text like %:query% order by -time", nativeQuery = true)
  int getCountOfPostsByQuery(String query);

  @Query(value = "select count(*) from post where is_active = 1"
      + " and moderation_status = 'ACCEPTED'"
      , nativeQuery = true)
  int getPosts2ShowCount();

  @Query(value = "select count(*) from post where is_active = 1"
      + " and moderation_status = 'NEW'"
      , nativeQuery = true)
  int getNewPostsCount();

  @Query(value = "select count(*) from post where is_active = 1"
      + " and moderation_status = 'DECLINED'"
      + " and moderator_id = :moderator_id"
      , nativeQuery = true)
  int getMyDeclinedCount(@Param(value = "moderator_id") int moderator_id);

  @Query(value = "select count(*) from post where is_active = 1"
      + " and moderation_status = 'ACCEPTED'"
      + " and moderator_id = :moderator_id"
      , nativeQuery = true)
  int getMyAcceptedCount(@Param(value = "moderator_id") int moderator_id);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status = 'NEW' order by -time", nativeQuery = true)
  List<Post> getPosts2Moderate(Pageable page);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status = 'DECLINED'"
          + " and moderator_id = :moderator_id"
          + " order by -time", nativeQuery = true)
  List<Post> getDeclinedPosts(@Param(value = "moderator_id") int moderator_id, Pageable page);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status = 'ACCEPTED'"
          + " and moderator_id = :moderator_id"
          + " order by -time", nativeQuery = true)
  List<Post> getAcceptedPosts(@Param(value = "moderator_id") int moderator_id, Pageable page);

  @Query(value =
      "SELECT * FROM post where is_active = 0"
          + " and user_id = :user_id"
          + " order by -time", nativeQuery = true)
  List<Post> getMyInactivePosts(@Param(value = "user_id") int userId, Pageable pageable);

  @Query(value =
      "SELECT count(*) FROM post where is_active = 0"
          + " and user_id = :user_id", nativeQuery = true)
  int getMyInactivePostsCount(@Param(value = "user_id") int userId);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status = 'NEW'"
          + " and user_id = :user_id"
          + " order by -time", nativeQuery = true)
  List<Post> getMyNotAcceptedPosts(@Param(value = "user_id") int userId, Pageable pageable);

  @Query(value =
      "SELECT count(*) FROM post where is_active = 1 "
          + "and moderation_status = 'NEW'"
          + " and user_id = :user_id", nativeQuery = true)
  int getMyNotAcceptedPostsCount(@Param(value = "user_id") int userId);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status = 'DECLINED'"
          + " and user_id = :user_id"
          + " order by -time", nativeQuery = true)
  List<Post> getMyDeclinedPosts(@Param(value = "user_id") int userId, Pageable pageable);

  @Query(value =
      "SELECT count(*) FROM post where is_active = 1 "
          + "and moderation_status = 'DECLINED'"
          + " and user_id = :user_id", nativeQuery = true)
  int getMyDeclinedPostsCount(@Param(value = "user_id") int userId);

  @Query(value =
      "SELECT * FROM post where is_active = 1 "
          + "and moderation_status = 'ACCEPTED'"
          + " and user_id = :user_id"
          + " order by -time", nativeQuery = true)
  List<Post> getMyAcceptedPosts(@Param(value = "user_id") int userId, Pageable pageable);

  @Query(value =
      "SELECT count(*) FROM post where is_active = 1 "
          + "and moderation_status = 'ACCEPTED'"
          + " and user_id = :user_id", nativeQuery = true)
  int getMyAcceptedPostsCount(@Param(value = "user_id") int userId);

  @Query(value = "select * from post where id = :post_id and moderation_status = 'ACCEPTED'"
      , nativeQuery = true)
  Optional<Post> findAcceptedPostById(@Param(value = "post_id") int postId);

  @Query(value = "SELECT"
      + " count(*) as postsCount,"
      + " (select count(value) from post_vote"
      + " right join post p on post_vote.post_id = p.id"
      + " where value > 0 and p.is_active = 1"
      + " and p.moderation_status = 'ACCEPTED') as likesCount, "
      + " (select count(value) from post_vote"
      + " right join post p on post_vote.post_id = p.id"
      + " where value < 0 and p.is_active = 1"
      + " and p.moderation_status = 'ACCEPTED') as dislikesCount,"
      + " sum(view_count) as viewsCount,"
      + " min(post.time) as firstPublication"
      + " FROM post where is_active = 1 and moderation_status = 'ACCEPTED'", nativeQuery = true)
  Statistic getBlogStatistic();

  @Query(value = "SELECT"
      + " count(*) as postsCount,"
      + " (select count(value) from post_vote"
      + " right join post p on post_vote.post_id = p.id"
      + " where value > 0 and p.is_active = 1 and p.user_id = :user_id"
      + " and p.moderation_status = 'ACCEPTED') as likesCount, "
      + " (select count(value) from post_vote"
      + " right join post p on post_vote.post_id = p.id"
      + " where value < 0 and p.is_active = 1 and p.user_id = :user_id"
      + " and p.moderation_status = 'ACCEPTED') as dislikesCount,"
      + " sum(view_count) as viewsCount,"
      + " min(post.time) as firstPublication"
      + " FROM post where is_active = 1 and moderation_status = 'ACCEPTED'"
      + " and user_id = :user_id", nativeQuery = true)
  Statistic getPersonalBlogStatistic(@Param("user_id") int userId);
}