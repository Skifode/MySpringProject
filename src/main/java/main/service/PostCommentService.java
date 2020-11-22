package main.service;

import java.util.Date;
import java.util.TreeMap;
import main.api.request.AddCommentRequest;
import main.api.response.ResultErrorsResponse;
import main.model.PostComment;
import main.repository.PostCommentsRepository;
import main.repository.PostsRepository;
import main.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {

  private final PostCommentsRepository postCommentsRepository;
  private final PostsRepository postsRepository;
  private final UsersRepository usersRepository;

  @Autowired
  public PostCommentService(PostCommentsRepository postCommentsRepository,
      PostsRepository postsRepository,
      UsersRepository usersRepository) {
    this.postCommentsRepository = postCommentsRepository;
    this.postsRepository = postsRepository;
    this.usersRepository = usersRepository;
  }

  public ResponseEntity<?> addComment(AddCommentRequest request, String email) {
    TreeMap<String, String> errorsMap = new TreeMap<>();
    String text = request.getText();
    HttpStatus status;

    if (text.length() < 6) {
      errorsMap.put("text", "Текст комментария не задан или слишком короткий");
      status = HttpStatus.BAD_REQUEST;
    } else if (!usersRepository.existsByEmail(email)) {
      errorsMap.put("text", "Ой. Перезагрузите страницу");
      status = HttpStatus.UNAUTHORIZED;
    } else if (!postsRepository.existsById(request.getPostId())) {
      errorsMap.put("text", "Кажется, этого поста больше нет");
      status = HttpStatus.NOT_FOUND;
    } else {
      PostComment comment = new PostComment();
      int userId = usersRepository.findByEmail(email).getId();
      Integer parentId = request.getParentId() == 0 ? null : request.getParentId();
      status = HttpStatus.OK;

      comment.setParentId(parentId);
      comment.setPostId(request.getPostId());
      comment.setText(text);
      comment.setTime(new Date());
      comment.setUserId(userId);

      return new ResponseEntity<>(ResultErrorsResponse
          .builder()
          .id(postCommentsRepository
              .save(comment)
              .getId())
          .build(), status);
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(false)
        .errors(errorsMap)
        .build(), status);
  }
}
