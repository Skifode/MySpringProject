package main.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import main.model.Posts;
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

    ArrayList<Posts> posts = new ArrayList<>();
    postsRepository.findAll().forEach(posts::add);

    ModelMap[] modelMass = new ModelMap[count];

    for(int i = 0; i < count; i++) {
      ModelMap modelMap = new ModelMap();

      ModelMap mm2 = new ModelMap();
      mm2.addAttribute("id", posts.get(i).getUser().getId());
      mm2.addAttribute("name", posts.get(i).getUser().getName());

      String date = posts.get(i).getTime().toString();

      /*
      ГОСПОДИ, ПРОСТИ ЗА ЭТОТ КОД
      Я ПРОСТО ПЫТАЮСЬ ПОНЯТЬ КАК ЭТО РАБОТАЕТ
       */

      modelMap.addAttribute("id", posts.get(i).getId());
      modelMap.addAttribute("timestamp", LocalDate.parse(date).atStartOfDay().atZone(ZoneId.of("UTC")).toEpochSecond());
      modelMap.addAttribute("user", mm2);
      modelMap.addAttribute("title", posts.get(i).getTitle());
      modelMap.addAttribute("announce", "АНОНС ПОСТА");
      modelMap.addAttribute("likeCount", 36);
      modelMap.addAttribute("dislikeCount", 3);
      modelMap.addAttribute("commentCount", 15);
      modelMap.addAttribute("viewCount", 55);

    modelMass[i] = modelMap;
    }

    model.addAttribute("count", count);
    model.addAttribute("posts", modelMass);

    return new ResponseEntity<>(model, HttpStatus.OK);
  }
}
