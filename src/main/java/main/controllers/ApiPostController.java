package main.controllers;

import java.util.List;
import main.api.response.PostResponse;
import main.api.response.SinglePostResponse;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

  @Autowired
  private PostService postService;

  @GetMapping("/api/post") public ResponseEntity<Model> getPosts(
      @RequestParam int offset, @RequestParam int limit,
      @RequestParam String mode, Model response) {
    List<PostResponse> posts = postService.getPosts(offset,limit,mode);
    response.addAttribute("count", postService.getPostsRepository().count());
    response.addAttribute("posts", posts);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
    //тут нужно принимать optional-ы и выдавать 404 ?
    //у меня там нет никакой обработки, если записи не найдено или если is_active = false, пока-что

  @GetMapping("/api/post/{id}") public ResponseEntity<SinglePostResponse> getPostInfo(
      @PathVariable(value = "id") int id) {
      return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
  }
}
