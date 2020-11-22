package main.service;

import main.api.response.SettingsResponse;
import main.repository.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service @Component
public class SettingsService {

  @Value("${settings.positive}")
  private String positive;

  private final GlobalSettingsRepository repository;

  @Autowired
  public SettingsService(GlobalSettingsRepository repository) {
    this.repository = repository;
  }

  public SettingsResponse getGlobalSettings() {
    SettingsResponse response = new SettingsResponse();

    repository.findAll().forEach(sett -> {
      String code = sett.getCode();

      switch (code) {
        case "MULTIUSER_MODE" -> response.setMultiuserMode(sett.getValue().equals(positive));
        case "POST_PREMODERATION" -> response.setPostModeration(sett.getValue().equals(positive));
        case "STATISTICS_IS_PUBLIC" -> response
            .setStatisticsIsPublic(sett.getValue().equals(positive));
        default -> System.out.println("Ошибка при поиске нужной настройки");
      }
    });
    return response;
  }
}
