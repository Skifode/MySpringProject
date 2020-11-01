package main.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import main.api.response.TagListResponse;
import main.api.response.TagResponse;
import main.model.Tag;
import main.repositories.PostsRepository;
import main.repositories.Tag2PostRepository;
import main.repositories.TagsRepository;
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
    List<TagResponse> response = new ArrayList<>();
    double countOfPosts = postsRepository.count();

    AtomicReference<Double> normWeight = new AtomicReference<>((double) 0);
    Iterable<Tag> tagsList = tagsRepository.findAll();


    for (Tag tag : tagsList
    ) {
      String name = tag.getName();
      double weight = tag2PostRepository.findByTagId(tag.getId()).size()/countOfPosts;

      if (weight>normWeight.get()) {
        normWeight.set(weight);
      }

      response.add(TagResponse.builder()
          .name(name)
          .weight(weight)
          .build());
    }
    response.forEach(tagResponse -> tagResponse
        .setWeight(tagResponse.getWeight()*(1/normWeight.get())));
    return TagListResponse
        .builder()
        .tags(response)
        .build();
  }
}
