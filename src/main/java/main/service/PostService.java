package main.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import main.api.response.PostCommentsResponse;
import main.api.response.PostResponse;
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

  public List<PostResponse> getPosts(int offset, int limit, String mode) {

    ArrayList<PostResponse> serviceObjects = new ArrayList<>();

    switch (mode)
    {
      case "recent" : new ArrayList<>(postsRepository.findByRecentIdFromTo(offset, offset+limit))
          .forEach(pos -> serviceObjects.add(new PostResponse(pos)));
        break;
      case "popular" : new ArrayList<>(postsRepository.findByPopularIdFromTo(offset, offset+limit))
          .forEach(pos -> serviceObjects.add(new PostResponse(pos)));
        break;
      case "best" : new ArrayList<>(postsRepository.findByBestIdFromTo(offset, offset+limit))
          .forEach(pos -> serviceObjects.add(new PostResponse(pos)));
        break;
      case "early" : new ArrayList<>(postsRepository.findByEarlyIdFromTo(offset, offset+limit))
          .forEach(pos -> serviceObjects.add(new PostResponse(pos)));
        break;
    }

    return serviceObjects;
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