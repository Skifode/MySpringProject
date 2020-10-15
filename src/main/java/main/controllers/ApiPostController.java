package main.controllers;

import java.util.Date;
import main.api.response.PostsListResponse;
import main.api.response.SinglePostResponse;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

  @GetMapping("/api/post/byTag")
  public ResponseEntity<PostsListResponse> getPostByTag(
      @RequestParam int offset,
      @RequestParam int limit,
      @RequestParam String tag) {
    return new ResponseEntity<>(postService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
  }
  @GetMapping("/api/post/byDate")
  public ResponseEntity<PostsListResponse> getPostByDate(
      @RequestParam int offset,
      @RequestParam int limit,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
    return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
  }
  @GetMapping("/api/post/search")
  public ResponseEntity<PostsListResponse> getPostByQuery(
      @RequestParam int offset,
      @RequestParam int limit,
      @RequestParam(defaultValue = "") String query) {
    return new ResponseEntity<>(postService.getPostsByQuery(offset, limit, query), HttpStatus.OK);
  }
}
