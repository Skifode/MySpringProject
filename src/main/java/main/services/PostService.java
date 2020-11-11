package main.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.Data;
import main.api.request.AddPostRequest;
import main.api.response.CalendarResponse;
import main.api.response.PostCommentsResponse;
import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.api.response.ResultErrorsResponse;
import main.api.response.SinglePostResponse;
import main.data.UploadType;
import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.model.Tag2Post;
import main.repositories.CountsPostsByDate;
import main.repositories.PostCommentsRepository;
import main.repositories.PostsRepository;
import main.repositories.Tag2PostRepository;
import main.repositories.TagsRepository;
import main.repositories.UsersRepository;
import main.repositories.YearsListForCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Data
@Service
public class PostService {

  private final PostsRepository postsRepository;
  private final PostCommentsRepository commentsRepository;
  private final Tag2PostRepository tag2PostRepository;
  private final TagsRepository tagsRepository;
  private final UsersRepository usersRepository;
  private final StorageService storageService;

  @Autowired
  public PostService(
      PostsRepository postsRepository,
      PostCommentsRepository commentsRepository,
      Tag2PostRepository tag2PostRepository,
      TagsRepository tagsRepository,
      UsersRepository usersRepository,
      StorageService storageService) {
    this.postsRepository = postsRepository;
    this.commentsRepository = commentsRepository;
    this.tag2PostRepository = tag2PostRepository;
    this.tagsRepository = tagsRepository;
    this.usersRepository = usersRepository;
    this.storageService = storageService;
  }

  public PostsListResponse getPosts(int offset, int limit, String mode) {

    Pageable pageable = PageRequest.of(offset / limit, limit);
    List<Post> posts = switch (mode) {
      case "recent" -> postsRepository.findByRecent(pageable);
      case "popular" -> postsRepository.findByPopular(pageable);
      case "best" -> postsRepository.findByBest(pageable);
      case "early" -> postsRepository.findByEarly(pageable);
      default -> new ArrayList<>();
    };

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
    List<PostComment> commentsList = commentsRepository.getByPostId(post.getId());
    if (commentsList.size() > 0) {
      for (PostComment com : commentsList) {
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

    Pageable pageable = PageRequest.of(offset / limit, limit);

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

    Pageable pageable = PageRequest.of(offset / limit, limit);

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

    Pageable pageable = PageRequest.of(offset / limit, limit);

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

    List<Integer> allYears = postsRepository
        .getYears().stream()
        .map(YearsListForCalendar::getYears2Calendar)
        .collect(Collectors.toList());

    Map<String, Integer> posts = postsRepository
        .getCountPostsGroupByDate(year).stream()
        .collect(Collectors
            .toMap(CountsPostsByDate::getFormattedDate, CountsPostsByDate::getCount));

    return CalendarResponse.builder().years(allYears).posts(posts).build();
  }

  public ResponseEntity<?> addNewPost(AddPostRequest request, String email) {
    TreeMap<String, String> errors = new TreeMap<>();
    int userId = usersRepository.findByEmail(email).getId();
    String text = request.getText();
    String title = request.getTitle();

    if (request.getTitle() == null || title.length() < 3) {
      errors.put("title", "Заголовок должен быть длиннее 3 символов");
    }
    if (request.getText() == null || text.length() < 50) {
      errors.put("text", "Текст публикации слишком короткий");
    }
    if (errors.isEmpty()) {
      Post post = new Post();
      Date now = new Date();
      Date date = request.getTimestamp().before(now) ? now : request.getTimestamp();

      for (String tagName : request.getTags()) {
        Tag tag = tagsRepository.findByName(tagName);
        post.addTag(Objects.requireNonNullElseGet(tag, () -> new Tag(tagName)));
      }

      post.setActive(request.isActive());
      post.setText(text);
      post.setTitle(title);
      post.setTime(date);
      post.setUserId(userId);
      postsRepository.save(post);

      return new ResponseEntity<>(ResultErrorsResponse
          .builder()
          .result(true)
          .build(), HttpStatus.OK);
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(false)
        .errors(errors)
        .build(), HttpStatus.BAD_REQUEST);
  }

  public ResponseEntity<?> uploadImage2Post(MultipartFile file) {
    TreeMap<String, String> errors = new TreeMap<>();
    String path = "";

    try {
      path = storageService.saveImage(file, UploadType.IMAGE);
    } catch (IOException ex) {
      errors.put("photo", ex.getLocalizedMessage());
    }

    if (errors.isEmpty()) {
      return new ResponseEntity<>(path, HttpStatus.OK);
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(false)
        .errors(errors)
        .build(), HttpStatus.BAD_REQUEST);
  }
}