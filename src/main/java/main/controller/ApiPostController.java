package main.controller;

import java.security.Principal;
import java.util.Date;
import main.api.request.AddPostRequest;
import main.api.request.VoteRequest;
import main.api.response.PostsListResponse;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

  private final PostService postService;

  @Autowired
  public ApiPostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/api/post") public ResponseEntity<?> getPosts(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "recent") String mode) {
    return postService.getPosts(offset,limit,mode);
  }

  @GetMapping("/api/post/{id}") public ResponseEntity<?> getPostInfo(
      @PathVariable(value = "id") int id, Principal principal) {
      return postService.getPostById(id, principal == null ? "" : principal.getName());
  }

  @GetMapping("/api/post/byTag")
  public ResponseEntity<PostsListResponse> getPostByTag(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "Java") String tag) {
    return new ResponseEntity<>(postService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
  }

  @GetMapping("/api/post/byDate")
  public ResponseEntity<PostsListResponse> getPostByDate(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
    return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
  }

  @GetMapping("/api/post/search")
  public ResponseEntity<PostsListResponse> getPostByQuery(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "") String query) {
    return new ResponseEntity<>(postService.getPostsByQuery(offset, limit, query), HttpStatus.OK);
  }

  @PostMapping("/api/post")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<?> addNewPost(
      @RequestBody AddPostRequest request, Principal principal) {
    return postService.addNewPost(request, principal.getName());
  }

  @GetMapping("/api/post/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ResponseEntity<?> getPosts2Moderate(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "new") String status,
      Principal principal) {
    return postService.posts2moderate(offset, limit, status, principal.getName());
  }

  @PostMapping("/api/post/like")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<?> setLike(@RequestBody VoteRequest request, Principal principal) {
    return postService.setLike(request.getPostId(), principal.getName(), (byte) 1);
  }

  @PostMapping("/api/post/dislike")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<?> setDislike(@RequestBody VoteRequest request, Principal principal) {
    return postService.setLike(request.getPostId(), principal.getName(), (byte) -1);
  }

  @GetMapping("/api/post/my")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<?> getMyPosts(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "") String status,
      Principal principal) {
    return postService.getMyPosts(offset, limit, status, principal.getName());
  }

  @PutMapping("/api/post/{id}")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity<?> putNewPost(
      @RequestBody AddPostRequest request, Principal principal,
      @PathVariable(value = "id") int id) {
    return postService.editPost(request, principal.getName(), id);
  }
}
