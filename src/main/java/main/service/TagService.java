package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import main.api.response.TagListResponse;
import main.api.response.TagResponse;
import main.repository.PostsRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  private final TagsRepository tagsRepository;
  private final PostsRepository postsRepository;
  private final Tag2PostRepository tag2PostRepository;

  @Autowired
  public TagService(TagsRepository tagsRepository, PostsRepository postsRepository,
      Tag2PostRepository tag2PostRepository) {
    this.tagsRepository = tagsRepository;
    this.postsRepository = postsRepository;
    this.tag2PostRepository = tag2PostRepository;
  }

  public TagListResponse getTagList() {
    List<TagResponse> tagResponseList = new ArrayList<>();
    double countOfPosts = postsRepository.getPosts2ShowCount();

    AtomicReference<Double> normWeight = new AtomicReference<>((double) 0);

    tagsRepository.findAllAcceptedTags().forEach(tag -> {
      String name = tag.getName();
      int id = tag.getId();
      double weight = tag2PostRepository.findByTagId(id).size() / countOfPosts;
      double value = Math.max(weight, normWeight.get());
      normWeight.set(value);

      tagResponseList.add(TagResponse.builder()
          .name(name)
          .weight(weight * (1 / normWeight.get()))
          .build());
    });
    return TagListResponse
        .builder()
        .tags(tagResponseList)
        .build();
  }
}
