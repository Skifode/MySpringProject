package main.service;

import java.util.TreeMap;
import main.api.request.ChangePasswordRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.LoginResponse;
import main.api.response.ResultErrorsResponse;
import main.api.response.UserLoginResponse;
import main.model.User;
import main.repository.CaptchaCodesRepository;
import main.repository.PostsRepository;
import main.repository.UsersRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Value("${blog.path}")
  private String blogPath;

  private final UsersRepository usersRepository;
  private final CaptchaCodesRepository captchaCodesRepository;
  private final AuthenticationManager authenticationManager;
  private final PostsRepository postsRepository;
  private final MailService mailService;

  @Autowired
  public UserService(
      AuthenticationManager authenticationManager,
      UsersRepository usersRepository,
      CaptchaCodesRepository captchaCodesRepository,
      PostsRepository postsRepository,
      MailService mailService) {
    this.authenticationManager = authenticationManager;
    this.usersRepository = usersRepository;
    this.captchaCodesRepository = captchaCodesRepository;
    this.postsRepository = postsRepository;
    this.mailService = mailService;
  }

  public LoginResponse getAuth(LoginRequest request) {
    User currentUser = usersRepository.findByEmail(request.getEmail());

    if (currentUser != null && new BCryptPasswordEncoder(12)
        .matches(request.getPassword(), currentUser.getPassword())) {

      Authentication authentication = authenticationManager
          .authenticate(
              new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      return getLoginResponse(request.getEmail());
    } else {
      return LoginResponse.builder()
          .result(false)
          .build();
    }
  }

  public LoginResponse getLoginResponse(String email) {
    User currentUser = usersRepository.findByEmail(email);
    int count = currentUser.isModerator() ? postsRepository.getNewPostsCount() : 0;

    UserLoginResponse userLoginResponse = UserLoginResponse.builder()
        .email(currentUser.getEmail())
        .id(currentUser.getId())
        .moderation(currentUser.isModerator())
        .moderationCount(count)
        .photo(currentUser.getPhoto())
        .name(currentUser.getName()).build();
    return LoginResponse.builder()
        .result(true)
        .userLoginResponse(userLoginResponse)
        .build();
  }

  public ResultErrorsResponse getRegisterResponse(RegisterRequest request) {

    String email = request.getEmail().trim();
    String pass = new BCryptPasswordEncoder(12).encode(request.getPassword().trim());
    String name = request.getName().trim();

    String captcha = request.getCaptcha().trim().toLowerCase();
    String captchaSecret = request.getCaptchaSecret();

    TreeMap<String, String> errorsMap = new TreeMap<>();

    validatePassAndCaptcha(errorsMap, pass, captcha, captchaSecret);
    validateName(errorsMap, name);
    validateEmail(errorsMap, email);

    if (errorsMap.isEmpty()) {
      saveUser(email, pass, name);
      return ResultErrorsResponse.builder().result(true).build();
    } else {
      return ResultErrorsResponse.builder().result(false).errors(errorsMap).build();
    }
  }

  public void saveUser(String email, String pass, String name) {
    usersRepository.save(new User(email, pass, name));
  }

  public ResponseEntity<?> restorePassword(String email) {
    ResultErrorsResponse response;
    if (email.isBlank() || !usersRepository.existsByEmail(email)) {
      response = ResultErrorsResponse.builder().result(false).build();
    } else {
      String code = RandomString.make(50);

      User user = usersRepository.findByEmail(email);
      user.setCode(code);
      usersRepository.save(user);

      mailService.sendEMail(email,
          "Ссылка для восстановления пароля DevPub",
          blogPath + "login/change-password/" + code);

      response = ResultErrorsResponse.builder().result(true).build();
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
    TreeMap<String, String> errorsMap = new TreeMap<>();

    String captcha = request.getCaptcha().trim();
    String captchaSecret = request.getCaptchaSecret();
    String pass = request.getPassword().trim();
    String code = request.getCode();
    User user = usersRepository.findByCode(code);
    validatePassAndCaptcha(errorsMap, pass, captcha, captchaSecret);

    if (user == null) {
      errorsMap.put("code",
          "Ссылка для восстановления пароля устарела или не существует." +
              "<a href=\"/login/restore-password\">Запросить ссылку снова</a>");
    } else if (errorsMap.isEmpty()) {
      user.setPassword(new BCryptPasswordEncoder(12).encode(pass));
      user.setCode(null);
      usersRepository.save(user);
      return new ResponseEntity<>(ResultErrorsResponse.builder()
          .result(true)
          .build(), HttpStatus.OK);
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .errors(errorsMap)
        .result(false).build(), HttpStatus.OK);
  }

  private void validatePassAndCaptcha(
      TreeMap<String, String> errorsMap,
      String pass,
      String captcha,
      String captchaSecret) {
    if (!captchaCodesRepository.existsByCode(captcha)
        || !captchaCodesRepository.findByCode(captcha)
        .getSecretCode().equals(captchaSecret)) {
      errorsMap.put("captcha",
          "Код с картинки введён неверно. Внимание! На картинке кирилица!");
    }
    if (pass.length() < 6 || pass.length() > 255) {
      errorsMap.put("password",
          "Пароль должен быть длиннее 6 символов и короче 255");
    }
  }

  private void validateEmail(TreeMap<String, String> errorsMap, String email) {
    if (usersRepository.existsByEmail(email)) {
      errorsMap.put("email", "e-mail " + email + " уже зарегистрирован");
    }
  }

  private void validateName(TreeMap<String, String> errorsMap, String name) {
    if (name.toLowerCase().contains("admin")) {
      errorsMap.put("name", "Имя указано неверно");
    }
  }
}
