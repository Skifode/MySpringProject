package main.service;

import com.github.cage.Cage;
import java.util.Base64;
import java.util.GregorianCalendar;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repositories.CaptchaCodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service @EnableScheduling
public class CaptchaService {

  private static final String ATTRIBUTE = "data:image/png;base64, ";

    private final CaptchaCodesRepository codesRepository;

    @Autowired
    public CaptchaService(CaptchaCodesRepository codesRepository) {
      this.codesRepository = codesRepository;
    }

    public CaptchaResponse getResponse() {
      Cage cage = new CyrillicCage();

      String code = cage.getTokenGenerator().next();
      String img = Base64.getEncoder().encodeToString(cage.draw(code));
      String secretCode = new BCryptPasswordEncoder(4)
          .encode(img.substring(1000,1024)); //не лучшая идея?

      CaptchaCode newCode = new CaptchaCode(new GregorianCalendar().getTime(), code, secretCode);
      codesRepository.save(newCode);

      return CaptchaResponse.builder()
          .image(ATTRIBUTE + img)
          .secret(secretCode)
          .build();
    }

  @Scheduled(fixedRate = 3600000)
  public void deleteOld() {
    codesRepository.deleteOld();
  }
}
