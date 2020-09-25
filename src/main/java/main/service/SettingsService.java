package main.service;

import main.api.response.SettingsResponse;
import main.repositories.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

  @Autowired
  GlobalSettingsRepository repository;

  public SettingsResponse getGlobalSettings() {
    SettingsResponse response = new SettingsResponse();

    repository.findAll().forEach(sett -> {
      String code = sett.getCode();
      String positive = "YES"; // :)

      switch (code)
      {
        case "MULTIUSER_MODE" : response.setMultiuserMode(sett.getValue().equals(positive));
          break;
        case "POST_PREMODERATION" : response.setPostModeration(sett.getValue().equals(positive));
          break;
        case "STATISTICS_IS_PUBLIC" : response.setStatisticsIsPublic(sett.getValue().equals(positive));
          break;

        default:
          System.out.println("Ошибка при поиске нужной настройки");
          break;
      }
    });
    return response;
  }
}
