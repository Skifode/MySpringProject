package main.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import main.model.Post;
import main.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

  @Autowired
  private PostsRepository postsRepository;

  @GetMapping("/api/post")
  public ResponseEntity<Model> getPosts(Model model) {

    int count = (int) postsRepository.count();

    ArrayList<Post> postList = new ArrayList<>();
    postsRepository.findAll().forEach(postList::add);

    ModelMap[] modelMass = new ModelMap[count];

    for(int i = 0; i < count; i++) {
      Post post = postList.get(i);

      ModelMap modelMap = new ModelMap();

      ModelMap mm2 = new ModelMap();
      mm2.addAttribute("id", post.getUser().getId());
      mm2.addAttribute("name", post.getUser().getName());

      String date = post.getTime().toString();

      /*
      ГОСПОДИ, ПРОСТИ ЗА ЭТОТ КОД
      Я ПРОСТО ПЫТАЮСЬ ПОНЯТЬ КАК ЭТО РАБОТАЕТ
       */

      modelMap.addAttribute("id", post.getId());
      modelMap.addAttribute("timestamp", LocalDate.parse(date)
          .atStartOfDay()
          .atZone(ZoneId.of("UTC"))
          .toEpochSecond());

      modelMap.addAttribute("user", mm2);
      modelMap.addAttribute("title", post.getTitle());
      modelMap.addAttribute("announce", "АНОНС ПОСТА");
      modelMap.addAttribute("likeCount", post.getLikesCount());
      modelMap.addAttribute("dislikeCount", post.getDislikeCount());
      modelMap.addAttribute("commentCount", post.getCommentsList().size());
      modelMap.addAttribute("viewCount", 404);

    modelMass[i] = modelMap;
    }

    model.addAttribute("count", count);
    model.addAttribute("posts", modelMass);

    return new ResponseEntity<>(model, HttpStatus.OK);
  }
}
