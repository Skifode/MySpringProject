package main.controllers;

import main.api.request.RegisterRequest;
import main.api.response.RegisterResponse;
import main.model.Users;
import main.repositories.UsersRepository;
import main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

  @Autowired
  UsersRepository usersRepository;

  @Autowired
  UsersService usersService;

  @GetMapping("/api/auth/check")
  public ResponseEntity<Model> check(Model model) {
    //пусть будет затычка
    model.addAttribute("result", false);
    return new ResponseEntity<>(model, HttpStatus.OK);
  }

  @PostMapping(value = "/api/auth/register", produces = "application/json")
  public ResponseEntity<RegisterResponse> register(
      @RequestBody RegisterRequest register) {
    Users user = new Users(register.getEmail(), register.getPassword(), register.getName());
    return new ResponseEntity<>(usersService.getResponse(user), HttpStatus.OK);
  }
}
