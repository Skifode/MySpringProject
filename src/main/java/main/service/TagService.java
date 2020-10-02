package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import main.api.response.TagResponse;
import main.model.Tags;
import main.repositories.PostsRepository;
import main.repositories.Tag2PostRepository;
import main.repositories.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  @Autowired
  private TagsRepository tagsRepository;

  @Autowired
  private PostsRepository postsRepository;

  @Autowired
  private Tag2PostRepository tag2PostRepository;

  public List<TagResponse> getTagList() {
    List<TagResponse> response = new ArrayList<>();
    double countOfPosts = postsRepository.count();

    AtomicReference<Double> normWeight = new AtomicReference<>((double) 0);
    Iterable<Tags> tagsList = tagsRepository.findAll();


    for (Tags tag : tagsList
    ) {
      double weight = tag2PostRepository.getByTagId(tag.getId()).size()/countOfPosts;

      if (weight>normWeight.get()) {
        normWeight.set(weight);
      }
      response.add(new TagResponse(tag.getName(), weight));
    }
    response.forEach(tagResponse -> {
      tagResponse.setWeight(tagResponse.getWeight()*(1/normWeight.get()));
    });
    return response;
  }
}
