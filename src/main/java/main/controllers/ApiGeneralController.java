package main.controllers;

import main.api.response.SettingsResponse;
import main.api.response.InitResponse;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

  @Autowired
  private InitResponse initResponseInfo;

  @Autowired
  private SettingsService settings;

  @Autowired
  private TagService tagService;

  @GetMapping("/api/init")
  public ResponseEntity<InitResponse> init() {
    return new ResponseEntity<>(initResponseInfo, HttpStatus.OK);
  }

  @GetMapping("/api/tag")
  public ResponseEntity<Model> tags(Model model) {
    model.addAttribute("tags", tagService.getTagList());
    return new ResponseEntity<>(model, HttpStatus.OK);
  }

  @GetMapping("/api/settings")
  public ResponseEntity<SettingsResponse> settings() {
    return new ResponseEntity<>(settings.getGlobalSettings(), HttpStatus.OK);
  }
}
