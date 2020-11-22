package main.service;

import main.api.response.StatisticResponse;
import main.repository.PostsRepository;
import main.repository.Statistic;
import main.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

  private final PostsRepository postsRepository;
  private final UsersRepository usersRepository;

  @Autowired
  public StatisticService(PostsRepository postsRepository,
      UsersRepository usersRepository) {
    this.postsRepository = postsRepository;
    this.usersRepository = usersRepository;
  }

  public ResponseEntity<?> getAllStatistic() {
    Statistic statistic = postsRepository.getBlogStatistic();
    return new ResponseEntity<>(StatisticResponse.builder()
        .likesCount(statistic
            .getLikesCount())
        .dislikesCount(statistic
            .getDislikesCount())
        .firstPublication(statistic
            .getFirstPublication()
            .toInstant()
            .getEpochSecond())
        .postsCount(statistic
            .getPostsCount())
        .viewsCount(statistic
            .getViewsCount())
        .build(),HttpStatus.OK);
  }

  public ResponseEntity<?> getPersonalStatistic(String email) {
    if (usersRepository.existsByEmail(email)) {
      int id = usersRepository.findByEmail(email).getId();
      Statistic statistic = postsRepository.getPersonalBlogStatistic(id);
      return new ResponseEntity<>(StatisticResponse.builder()
          .likesCount(statistic
              .getLikesCount())
          .dislikesCount(statistic
              .getDislikesCount())
          .firstPublication(statistic
              .getFirstPublication()
              .toInstant()
              .getEpochSecond())
          .postsCount(statistic
              .getPostsCount())
          .viewsCount(statistic
              .getViewsCount())
          .build(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
