package main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

  @GetMapping("/api/auth/check")
  public ResponseEntity<Model> check(Model model) {
    //пусть будет затычка
    model.addAttribute("result", false);
    return new ResponseEntity<>(model, HttpStatus.OK);
  }
}
