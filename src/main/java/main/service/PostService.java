package main.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.Data;
import main.api.response.CalendarResponse;
import main.api.response.PostCommentsResponse;
import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.api.response.SinglePostResponse;
import main.model.Post;
import main.model.PostComments;
import main.model.Tag2Post;
import main.repositories.PostCommentsRepository;
import main.repositories.PostsRepository;
import main.repositories.Tag2PostRepository;
import main.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Data
@Service
public class PostService {

  @Autowired
  private PostsRepository postsRepository;

  @Autowired
  private PostCommentsRepository commentsRepository;

  @Autowired
  private Tag2PostRepository tag2PostRepository;

  @Autowired
  private TagsRepository tagsRepository;


  public PostsListResponse getPosts(int offset, int limit, String mode) {

    Pageable pageable = PageRequest.of(offset/limit, limit);
    List<Post> posts = new ArrayList<>();

    switch (mode) {
      case "recent" -> posts = postsRepository.findByRecent(pageable);
      case "popular" -> posts = postsRepository.findByPopular(pageable);
      case "best" -> posts = postsRepository.findByBest(pageable);
      case "early" -> posts = postsRepository.findByEarly(pageable);
    }
    List<PostResponse> postResponses = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    return PostsListResponse.builder()
        .posts(postResponses)
        .count(postsRepository.count())
        .build();
  }

  public SinglePostResponse getPostById(int id) { //ужс. переделаю
    Post post = postsRepository.findById(id).orElseThrow();
    post.incrementView();
    postsRepository.save(post);
    SinglePostResponse response = new SinglePostResponse(
        new PostResponse(post), post.isActive(), post.getText());

    ArrayList<PostCommentsResponse> comments = new ArrayList<>();
    List<PostComments> commentsList = commentsRepository.getByPostId(post.getId());
    if (commentsList.size() > 0) {
      for (PostComments com : commentsList) {
        comments.add(new PostCommentsResponse(com));
      }
    }

    List<String> tags = new ArrayList<>();
    List<Tag2Post> tagsInt = tag2PostRepository.findByPostId(post.getId());
    if (tagsInt.size() > 0) {
      tagsInt.forEach(tag -> tagsRepository.findById(tag.getTagId())
          .ifPresent(element -> tags.add(element.getName())));
    }
    response.setComments(comments);
    response.setTags(tags);
    return response;
  }

  public PostsListResponse getPostsByTag(int offset, int limit, String tag) {

    Pageable pageable = PageRequest.of(offset/limit, limit);

    int tagId = tagsRepository.findByName(tag).getId();
    int count = tag2PostRepository.findByTagId(tagId).size();
    List<Post> posts = new ArrayList<>(postsRepository.findByTag(pageable, tagId));

    List<PostResponse> postResponses = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    return PostsListResponse.builder()
        .posts(postResponses)
        .count(count)
        .build();

  }

  public PostsListResponse getPostsByDate(int offset, int limit, Date date) {

    Pageable pageable = PageRequest.of(offset/limit, limit);

    List<Post> posts = new ArrayList<>(postsRepository.findByDate(pageable, date));

    List<PostResponse> postResponses = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    return PostsListResponse.builder()
        .posts(postResponses)
        .count(postsRepository.getCountOfPostsByDate(date))
        .build();
  }

  public PostsListResponse getPostsByQuery(int offset, int limit, String query) {

    Pageable pageable = PageRequest.of(offset/limit, limit);

    List<Post> posts = new ArrayList<>(postsRepository.findPostsByQuery(pageable, query));

    List<PostResponse> postResponses = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    return PostsListResponse.builder()
        .posts(postResponses)
        .count(postsRepository.getCountOfPostsByQuery(query))
        .build();
  }

  public CalendarResponse getCalendar(int year) {
    ArrayList<Integer> allYears = new ArrayList<>();
    Map<String, Integer> posts = new TreeMap<>();

    postsRepository.getPostsGroupByYear().forEach(
        post -> allYears.add(Integer.parseInt(post
            .getTime()
            .toString()
            .split("-")[0])));

    postsRepository.getPostsByYear(year).forEach(
        post -> posts.put(post
                .getTime()
                .toString()
                .substring(0,10),
            postsRepository.getCountOfPostsByDate(post.getTime())));

    return CalendarResponse.builder().years(allYears).posts(posts).build();
  }
}