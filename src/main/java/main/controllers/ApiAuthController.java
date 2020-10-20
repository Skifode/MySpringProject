package main.controllers;

import java.security.Principal;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.LoginResponse;
import main.api.response.LogoutResponse;
import main.api.response.RegisterResponse;
import main.api.response.UserLoginResponse;
import main.model.Users;
import main.repositories.UsersRepository;
import main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

  private final AuthenticationManager authenticationManager;
  private final UsersRepository usersRepository;

  @Autowired
  UsersService usersService;

  @Autowired
  public ApiAuthController(
      AuthenticationManager authenticationManager,
      UsersRepository usersRepository) {
    this.authenticationManager = authenticationManager;
    this.usersRepository = usersRepository;
  }

  @GetMapping("/check")
  public ResponseEntity<LoginResponse> check(Principal principal) {
    if (principal == null) {
      return new ResponseEntity<>(new LoginResponse(), HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(getLoginResponse(principal.getName()), HttpStatus.OK);
    }
  }

  @GetMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(Principal principal) {
    SecurityContextHolder.clearContext();
      return new ResponseEntity<>(new LogoutResponse(), HttpStatus.OK);
  }

  @PostMapping("/login") //хочу спать. перенесу в сервис
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

    Authentication authentication = authenticationManager
        .authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    User user = (User) authentication.getPrincipal();

    return new ResponseEntity<>(getLoginResponse(user.getUsername()), HttpStatus.OK);
  }

  private LoginResponse getLoginResponse(String email) { //и это тоже перенесу zzZZzZZZzzzzzzzzzzzz
    Users currentUser= usersRepository.findByEmail(email);

    UserLoginResponse userLoginResponse = new UserLoginResponse();
    userLoginResponse.setEmail(currentUser.getEmail());
    userLoginResponse.setId(currentUser.getId());
    userLoginResponse.setModeration(currentUser.isModerator());
    userLoginResponse.setPhoto(currentUser.getPhoto());
    userLoginResponse.setName((currentUser.getName()));


    LoginResponse response= new LoginResponse();
    response.setResult(true);
    response.setUserLoginResponse(userLoginResponse);
    return response;
  }

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity<RegisterResponse> register(
      @RequestBody RegisterRequest register) {
    Users user = new Users(register.getEmail(), register.getPassword(), register.getName());
    return new ResponseEntity<>(usersService.getResponse(user), HttpStatus.OK);
  }
}
