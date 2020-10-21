package main.controllers;

import java.security.Principal;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.LogoutResponse;
import main.api.response.RegisterResponse;
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
    if (principal == null) {
      return new ResponseEntity<>(
          LoginResponse.builder()
          .result(false)
          .build(), HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(userService.getLoginResponse(principal.getName()), HttpStatus.OK);
    }
  }

  @GetMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(Principal principal) {
    SecurityContextHolder.clearContext();
      return new ResponseEntity<>(new LogoutResponse(), HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return new ResponseEntity<>(userService.getAuth(request), HttpStatus.OK);
  }

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity<RegisterResponse> register(
      @RequestBody RegisterRequest request) {
    return new ResponseEntity<>(userService.getRegisterResponse(request), HttpStatus.OK);
  }

  @GetMapping("/captcha")
  public ResponseEntity<CaptchaResponse> captcha() {
    return new ResponseEntity<>(captchaService.getResponse(), HttpStatus.OK);
  }
}
