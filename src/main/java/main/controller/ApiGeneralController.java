package main.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import main.api.request.AddCommentRequest;
import main.api.request.ModerationStatusRequest;
import main.api.request.ProfileSettingsRequest;
import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagListResponse;
import main.service.PostCommentService;
import main.service.PostService;
import main.service.ProfileSettingsService;
import main.service.SettingsService;
import main.service.StatisticService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ApiGeneralController {

  private final InitResponse initResponseInfo;
  private final SettingsService settings;
  private final TagService tagService;
  private final PostService postService;
  private final ProfileSettingsService profileSettingsService;
  private final PostCommentService postCommentService;
  private final StatisticService statisticService;


  @Autowired
  public ApiGeneralController(InitResponse initResponseInfo, SettingsService settings,
      TagService tagService, PostService postService,
      ProfileSettingsService profileSettingsService,
      PostCommentService postCommentService,
      StatisticService statisticService) {
    this.initResponseInfo = initResponseInfo;
    this.settings = settings;
    this.tagService = tagService;
    this.postService = postService;
    this.profileSettingsService = profileSettingsService;
    this.postCommentService = postCommentService;
    this.statisticService = statisticService;
  }

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

  @PreAuthorize("hasAuthority('user:moderate')")
  @PutMapping("/api/settings")
  public ResponseEntity<?> putSettings(@RequestBody SettingsResponse request) {
    return settings.putGlobalSettings(request);
  }

  @GetMapping("/api/calendar")
  public ResponseEntity<CalendarResponse> calendar(
      @RequestParam(name = "year", defaultValue = "0") Integer year) {
    return new ResponseEntity<>(
        postService.getCalendar(year != 0 ?
            year : new GregorianCalendar().get(Calendar.YEAR)), HttpStatus.OK);
  }

  @PreAuthorize("hasAuthority('user:write')")
  @PostMapping(value = "/api/profile/my", consumes = "multipart/form-data")
  public ResponseEntity<?> multipartProfileChanges(
      @ModelAttribute ProfileSettingsRequest request, Principal principal) {
    return profileSettingsService.editProfile(request, principal.getName());
  }

  @PreAuthorize("hasAuthority('user:write')")
  @PostMapping(value = "/api/profile/my", consumes = "application/json")
  public ResponseEntity<?> otherProfileChanges(
      @RequestBody ProfileSettingsRequest request, Principal principal) {
    return profileSettingsService.editProfile(request, principal.getName());
  }

  @PreAuthorize("hasAuthority('user:write')")
  @PostMapping(value = "/api/image", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadImage(
      @RequestParam(name = "image") MultipartFile image ) {
    return postService.uploadImage2Post(image);
  }

  @PreAuthorize("hasAuthority('user:write')")
  @PostMapping(value = "/api/comment", consumes = "application/json")
  public ResponseEntity<?> addComment(@RequestBody AddCommentRequest request, Principal principal) {
    return postCommentService.addComment(request, principal.getName());
  }

  @PreAuthorize("hasAuthority('user:moderate')")
  @PostMapping(value = "/api/moderation", consumes = "application/json")
  public ResponseEntity<?> setModerationStatus(
      @RequestBody ModerationStatusRequest request,
      Principal principal) {
    return postService.setModerationStatus(
        request.getDecision(), request.getPostId(), principal.getName());
  }

  @GetMapping(value = "/api/statistics/all")
  public ResponseEntity<?> getAllStatistic() {
    return statisticService.getAllStatistic();
  }

  @PreAuthorize("hasAuthority('user:write')")
  @GetMapping(value = "/api/statistics/my")
  public ResponseEntity<?> getPersonalStatistic(Principal principal) {
    return statisticService.getPersonalStatistic(principal.getName());
  }
}
