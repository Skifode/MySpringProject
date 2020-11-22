package main.controller;

import java.security.Principal;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.LogoutResponse;
import main.api.response.ResultErrorsResponse;
import main.service.CaptchaService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {


  private final CaptchaService captchaService;
  private final UserService userService;

  @Autowired
  public ApiAuthController(
      UserService userService,
      CaptchaService captchaService) {
    this.userService = userService;
    this.captchaService = captchaService;
  }

  @GetMapping("/check")
  public ResponseEntity<LoginResponse> check(Principal principal) {
    LoginResponse loginResponse = principal == null ?
        LoginResponse.builder().result(false).build() :
        userService.getLoginResponse(principal.getName());

    return new ResponseEntity<>(loginResponse, HttpStatus.OK);
  }

  @GetMapping("/logout")
  public ResponseEntity<LogoutResponse> logout() {
    SecurityContextHolder.clearContext();
      return new ResponseEntity<>(new LogoutResponse(), HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return new ResponseEntity<>(userService.getAuth(request), HttpStatus.OK);
  }

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity<ResultErrorsResponse> register(
      @RequestBody RegisterRequest request) {
    return new ResponseEntity<>(userService.getRegisterResponse(request), HttpStatus.OK);
  }

  @GetMapping("/captcha")
  public ResponseEntity<CaptchaResponse> captcha() {
    return new ResponseEntity<>(captchaService.getResponse(), HttpStatus.OK);
  }
}
