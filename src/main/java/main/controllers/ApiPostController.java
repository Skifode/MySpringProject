package main.controllers;

import main.api.response.PostsListResponse;
import main.api.response.SinglePostResponse;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

  @Autowired
  private PostService postService;

  @GetMapping("/api/post") public ResponseEntity<PostsListResponse> getPosts(
      @RequestParam int offset,
      @RequestParam int limit,
      @RequestParam String mode) {
    return new ResponseEntity<>(postService.getPosts(offset,limit,mode), HttpStatus.OK);
  }

  @GetMapping("/api/post/{id}") public ResponseEntity<SinglePostResponse> getPostInfo(
      @PathVariable(value = "id") int id) {
      return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
  }
}
