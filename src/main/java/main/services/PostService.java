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
import main.data.Status;
import main.data.UploadType;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVote;
import main.model.Tag;
import main.model.Tag2Post;
import main.repositories.CountsPostsByDate;
import main.repositories.PostCommentsRepository;
import main.repositories.PostVotesRepository;
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
  private final PostVotesRepository postVotesRepository;

  @Autowired
  public PostService(
      PostsRepository postsRepository,
      PostCommentsRepository commentsRepository,
      Tag2PostRepository tag2PostRepository,
      TagsRepository tagsRepository,
      UsersRepository usersRepository,
      StorageService storageService,
      PostVotesRepository postVotesRepository) {
    this.postsRepository = postsRepository;
    this.commentsRepository = commentsRepository;
    this.tag2PostRepository = tag2PostRepository;
    this.tagsRepository = tagsRepository;
    this.usersRepository = usersRepository;
    this.storageService = storageService;
    this.postVotesRepository = postVotesRepository;
  }

  public ResponseEntity<?> getPosts(int offset, int limit, String mode) {

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

    return new ResponseEntity<>(PostsListResponse.builder()
        .posts(postResponses)
        .count(postsRepository.getPosts2ShowCount())
        .build(), HttpStatus.OK);
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

  public ResponseEntity<?> posts2moderate(int offset, int limit, String status, String email) {
    int moderator_id = usersRepository.findByEmail(email).getId();
    Pageable pageable = PageRequest.of(offset / limit, limit);

    int count = switch (status) {
      case "new" -> postsRepository.getNewPostsCount();
      case "declined" -> postsRepository.getMyDeclinedCount(moderator_id);
      case "accepted" -> postsRepository.getMyAcceptedCount(moderator_id);
      default -> 0;
    };

    List<Post> posts = switch (status) {
      case "new" -> postsRepository.getPosts2Moderate(pageable);
      case "declined" -> postsRepository.getDeclinedPosts(moderator_id, pageable);
      case "accepted" -> postsRepository.getAcceptedPosts(moderator_id, pageable);
      default -> new ArrayList<>();
    };

    List<PostResponse> postResponses = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    return new ResponseEntity<>(PostsListResponse.builder()
        .posts(postResponses)
        .count(count)
        .build(), HttpStatus.OK);
  }

  public ResponseEntity<?> setModerationStatus(String decision, int postId, String email) {
    int moderId = usersRepository.findByEmail(email).getId();
    switch (decision) {
      case "accept" -> postsRepository.findById(postId)
          .ifPresent(p -> {
            p.setModerationStatus(Status.ACCEPTED);
            p.setModeratorId(moderId);
            postsRepository.save(p);
          });
      case "decline" -> postsRepository.findById(postId)
          .ifPresent(p -> {
            p.setModerationStatus(Status.DECLINED);
            p.setModeratorId(moderId);
            postsRepository.save(p);
          });
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public ResponseEntity<?> setLike(int postId, String email, byte value) {
    int userId = usersRepository.findByEmail(email).getId();
    PostVote postVote = postVotesRepository
        .findByUserIdAndPostId(userId, postId)
        .orElse(new PostVote());
    boolean result = postVote.getValue() != value;

    if (result) {
      postVote.setTime(new Date());
      postVote.setPostId(postId);
      postVote.setValue(value);
      postVote.setUserId(userId);
      postVotesRepository.save(postVote);
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(result)
        .build(), HttpStatus.OK);
  }

  public ResponseEntity<?> getMyPosts(int offset, int limit, String status, String email) {
    int userId = usersRepository.findByEmail(email).getId();
    Pageable pageable = PageRequest.of(offset / limit, limit);

    List<Post> posts = switch (status) {
      case "inactive" -> postsRepository.getMyInactivePosts(userId, pageable);
      case "pending" -> postsRepository.getMyNotAcceptedPosts(userId, pageable);
      case "declined" -> postsRepository.getMyDeclinedPosts(userId, pageable);
      case "published" -> postsRepository.getMyAcceptedPosts(userId, pageable);
      default -> new ArrayList<>();
    };

    int count = switch (status) {
      case "inactive" -> postsRepository.getMyInactivePostsCount(userId);
      case "pending" -> postsRepository.getMyNotAcceptedPostsCount(userId);
      case "declined" -> postsRepository.getMyDeclinedPostsCount(userId);
      case "published" -> postsRepository.getMyAcceptedPostsCount(userId);
      default -> 0;
    };

    List<PostResponse> postResponses = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    return new ResponseEntity<>(PostsListResponse.builder()
        .posts(postResponses)
        .count(count)
        .build(), HttpStatus.OK);
  }

  public ResponseEntity<?> putNewPost(AddPostRequest request, String name, int id) {
// спаааааааааааать
    return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
  }
}