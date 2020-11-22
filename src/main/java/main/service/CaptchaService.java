package main.service;

import com.github.cage.Cage;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.UUID;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaCodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service @EnableScheduling
public class CaptchaService {

  @Value("${captcha.response.attribute}")
  private String ATTRIBUTE;

    private final CaptchaCodesRepository codesRepository;

    @Autowired
    public CaptchaService(CaptchaCodesRepository codesRepository) {
      this.codesRepository = codesRepository;
    }

    public CaptchaResponse getResponse() {
      Cage cage = new CyrillicCage();

      String code = cage.getTokenGenerator().next();
      String img = Base64.getEncoder().encodeToString(cage.draw(code));
      String secretCode = UUID.randomUUID().toString();

      CaptchaCode newCode = new CaptchaCode(new GregorianCalendar().getTime(), code, secretCode);
      codesRepository.save(newCode);

      return CaptchaResponse.builder()
          .image(ATTRIBUTE + img)
          .secret(secretCode)
          .build();
    }

  @Scheduled(fixedRate = 3600000)
  public void deleteOldCaptcha() {
    codesRepository.deleteOldCaptcha();
  }
}
