package main.controllers;

import java.util.Calendar;
import java.util.GregorianCalendar;
import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagListResponse;
import main.service.PostService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

  @Autowired
  private InitResponse initResponseInfo;

  @Autowired
  private SettingsService settings;

  @Autowired
  private TagService tagService;

  @Autowired
  private PostService postService;

  @GetMapping("/api/init")
  public ResponseEntity<InitResponse> init() {
    return new ResponseEntity<>(initResponseInfo, HttpStatus.OK);
  }

  @GetMapping("/api/tag")
  public ResponseEntity<TagListResponse> tags() {
    return new ResponseEntity<>(tagService.getTagList(), HttpStatus.OK);
  }

  @GetMapping("/api/settings")
  public ResponseEntity<SettingsResponse> settings() {
    return new ResponseEntity<>(settings.getGlobalSettings(), HttpStatus.OK);
  }

  @GetMapping("/api/calendar")
  public ResponseEntity<CalendarResponse> calendar(
      @RequestParam(name = "year", defaultValue = "0") Integer year) {
    return new ResponseEntity<>(
        postService.getCalendar(year!=0 ?
            year : new GregorianCalendar().get(Calendar.YEAR)), HttpStatus.OK);
  }
}
