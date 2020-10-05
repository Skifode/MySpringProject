package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import main.api.response.PostCommentsResponse;
import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.api.response.SinglePostResponse;
import main.model.Post;
import main.model.PostComments;
import main.model.Tag2Post;
import main.repositories.PostCommentsRepository;
import main.repositories.PostsRepository;
import main.repositories.Tag2PostRepository;
import main.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class PostService {

  @Autowired
  private PostsRepository postsRepository;

  @Autowired
  private PostCommentsRepository commentsRepository;

  @Autowired
  private Tag2PostRepository tag2PostRepository;

  @Autowired
  private TagsRepository tagsRepository;

  //тут пока не разобрался до конца тоже

  public PostsListResponse getPosts(int offset, int limit, String mode) {

    List<Post> posts = new ArrayList<>();

    switch (mode)
    {
      case "recent" : posts = postsRepository.findByRecentIdFromTo(offset, offset + limit);
        break;
      case "popular":
        posts = postsRepository.findByPopularIdFromTo(offset, offset + limit);
        break;
      case "best":
        posts = postsRepository.findByBestIdFromTo(offset, offset + limit);
        break;
      case "early":
        posts = postsRepository.findByEarlyIdFromTo(offset, offset + limit);
        break;
    }
    List<PostResponse> postResponses = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    return PostsListResponse.builder()
        .posts(postResponses)
        .count(postsRepository.count())
        .build();
  }

  public SinglePostResponse getPostById(int id) {
    Post post = postsRepository.findById(id).orElseThrow();
    post.incrementView();
    postsRepository.save(post);
    SinglePostResponse response = new SinglePostResponse(
        new PostResponse(post), post.isActive(), post.getText());

    ArrayList<PostCommentsResponse> comments = new ArrayList<>();
    List<PostComments> commentsList = commentsRepository.getByPostId(post.getId());
    if (commentsList.size() > 0) {
      for (PostComments com : commentsList) {
        comments.add(new PostCommentsResponse(com));
      }
    }

    List<String> tags = new ArrayList<>();
    List<Tag2Post> tagsInt = tag2PostRepository.getByPostId(post.getId());
    if (tagsInt.size() > 0) {
      tagsInt.forEach(tag -> tagsRepository.findById(tag.getTagId())
          .ifPresent(element -> tags.add(element.getName())));
    }
    response.setComments(comments);
    response.setTags(tags);
    return response;
  }
}