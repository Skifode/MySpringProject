package main.controllers;

import main.model.GlobalSettings;
import main.repositories.GlobalSettingsRepository;
import main.repositories.Init;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

  private final Init initInfo = new Init();
  @Autowired
  private GlobalSettingsRepository globalSettingsRepository;

  @GetMapping("/api/init")
  public ResponseEntity<Init> init() {
    return new ResponseEntity<>(initInfo, HttpStatus.OK);
  }

  @GetMapping("/api/tag")
  public ResponseEntity<String> tags() {
    //пусть будет затычка
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/api/settings")
  public ResponseEntity<Model> settings(Model model) {

    /*

                  тут небольшой костыль для человечества,
                     но очень важный костыль для меня

                          !доделать потом!

     */

    model.addAttribute("MULTIUSER_MODE", globalSettingsRepository.getById(1)
        .getValue().equals("YES"));
    model.addAttribute("POST_PREMODERATION", globalSettingsRepository.getById(2)
        .getValue().equals("YES"));
    model.addAttribute("STATISTICS_IS_PUBLIC", globalSettingsRepository.getById(3)
        .getValue().equals("YES"));

    return new ResponseEntity<>(model, HttpStatus.OK);
  }
}
