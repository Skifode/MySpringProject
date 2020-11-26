package main.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
import main.data.Vote;
import main.model.Post;
import main.model.PostVote;
import main.model.Tag;
import main.model.User;
import main.repository.CountsPostsByDate;
import main.repository.PostCommentsRepository;
import main.repository.PostVotesRepository;
import main.repository.PostsRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagsRepository;
import main.repository.UsersRepository;
import main.repository.YearsListForCalendar;
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

  public ResponseEntity<?> getPostById(int postId, String email) {

    boolean access = false; //права доступа на скрытый пост
    Post post;

    if (usersRepository.existsByEmail(email)) {
      User user = usersRepository.findByEmail(email);
      access = user.isModerator(); // модератор может смотреть любой скрытый пост
      if (!access) {      //пользователь может смотреть только свои скрытые посты
        access = postsRepository.countByUserIdAndPostId(user.getId(), postId) == 1;
      }
    }

    try {
      post = access ?
          postsRepository.findById(postId).orElseThrow()
          : postsRepository.findAcceptedPostById(postId).orElseThrow();
    } catch (NoSuchElementException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    post.incrementView();
    postsRepository.save(post);

    List<PostCommentsResponse> comments = commentsRepository
        .getByPostId(post.getId())
        .stream()
        .map(PostCommentsResponse::new)
        .collect(Collectors
            .toList());

    List<String> tags = tagsRepository.findTagNamesByPostId(post.getId());

    Map<String, Object> user = new HashMap<>();
    user.put("id", post.getUser().getId());
    user.put("name", post.getUser().getName());

    return new ResponseEntity<>(SinglePostResponse.builder()
        .active(post.isActive())
        .text(post.getText())
        .comments(comments)
        .dislikeCount(post.getDislikeCount())
        .likeCount(post.getLikesCount())
        .id(post.getId())
        .tags(tags)
        .timestamp(post.getTime()
            .toInstant()
            .getEpochSecond())
        .title(post.getTitle())
        .user(user)
        .viewCount(post.getViewCount())
        .build(), HttpStatus.OK);
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

    TreeMap<String, String> errors = postIsCorrect(request);

    if (errors.isEmpty()) {
      return postAddResponse(new Post(), request, email);
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(false)
        .errors(errors)
        .build(), HttpStatus.BAD_REQUEST);
  }

  private TreeMap<String, String> postIsCorrect(AddPostRequest request) {
    TreeMap<String, String> errors = new TreeMap<>();
    String text = request.getText();
    String title = request.getTitle();

    if (request.getTitle() == null || title.length() < 3) {
      errors.put("title", "Заголовок должен быть длиннее 3 символов");
    }
    if (request.getText() == null || text.length() < 50) {
      errors.put("text", "Текст публикации слишком короткий");
    }
    return errors;
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
    Status status;

    try {
      status = switch (decision) {
        case "accept" -> Status.ACCEPTED;
        case "decline" -> Status.DECLINED;
        default -> throw new IllegalArgumentException();
      };
    } catch (IllegalArgumentException ia) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    postsRepository.findById(postId)
        .ifPresent(p -> {
          p.setModerationStatus(status);
          p.setModeratorId(moderId);
          postsRepository.save(p);
        });
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public ResponseEntity<?> setVote(int postId, String email, Vote vote) {

    byte value = switch (vote) {
      case LIKE -> (byte) 1;
      case DISLIKE -> (byte) -1;
    };

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

  public ResponseEntity<?> editPost(AddPostRequest request, String email, int id) {

    TreeMap<String, String> errors = postIsCorrect(request);

    if (errors.isEmpty()) {
      Post post;
      try {
        post = postsRepository.findById(id).orElseThrow();
        if (!usersRepository.findByEmail(email).isModerator()) {
          post.setModerationStatus(Status.NEW);
        }
      } catch (NoSuchElementException ex) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return postAddResponse(post, request, email);
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(false)
        .errors(errors)
        .build(), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<?> postAddResponse(Post post, AddPostRequest request, String email) {
    int userId = usersRepository.findByEmail(email).getId();
    Date now = new Date();
    Date date = request.getTimestamp().before(now) ? now : request.getTimestamp();

    for (String tagName : request.getTags()) {
      Tag tag = tagsRepository.findByName(tagName);
      post.addTag(Objects.requireNonNullElseGet(tag, () -> new Tag(tagName)));
    }

    post.setActive(request.isActive());
    post.setText(request.getText());
    post.setTitle(request.getTitle());
    post.setTime(date);
    post.setUserId(userId);
    postsRepository.save(post);

    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(true)
        .build(), HttpStatus.OK);
  }
}