package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CaptchaCodesRepository extends CrudRepository<CaptchaCode, Integer> {

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM captcha_code WHERE DATE_SUB(NOW(), INTERVAL 1 HOUR)"
      , nativeQuery = true)
  void deleteOldCaptcha();

  CaptchaCode findByCode(String code);
  boolean existsByCode(String code);

}
