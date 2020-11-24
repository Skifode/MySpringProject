package main.service;

import java.util.NoSuchElementException;
import main.api.response.SettingsResponse;
import main.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class SettingsService {

  @Value("${settings.positive}")
  private String positive;

  @Value("${settings.negative}")
  private String negative;

  private final GlobalSettingsRepository settingsRepository;

  @Autowired
  public SettingsService(GlobalSettingsRepository settingsRepository) {
    this.settingsRepository = settingsRepository;
  }

  public SettingsResponse getGlobalSettings() {
    SettingsResponse response = new SettingsResponse();

    settingsRepository.findAll().forEach(sett -> {
      String code = sett.getCode();

      switch (code) {
        case "MULTIUSER_MODE" -> response
            .setMultiuserMode(sett.getValue().equals(positive));

        case "POST_PREMODERATION" -> response
            .setPostModeration(sett.getValue().equals(positive));

        case "STATISTICS_IS_PUBLIC" -> response
            .setStatisticsIsPublic(sett.getValue().equals(positive));
      }
    });
    return response;
  }

  public ResponseEntity<?> putGlobalSettings(SettingsResponse request) {
    try {
      settingsRepository.findAll().forEach(sett -> {
        String code = sett.getCode();
        switch (code) {
          case "MULTIUSER_MODE" -> settingsRepository
              .findById(sett.getId()).orElseThrow()
              .setValue(request.isMultiuserMode() ? positive : negative);

          case "POST_PREMODERATION" -> settingsRepository
              .findById(sett.getId()).orElseThrow()
              .setValue(request.isPostModeration() ? positive : negative);

          case "STATISTICS_IS_PUBLIC" -> settingsRepository
              .findById(sett.getId()).orElseThrow()
              .setValue(request.isStatisticsIsPublic() ? positive : negative);
        }
        settingsRepository.save(sett);
      });
    } catch (NoSuchElementException ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
