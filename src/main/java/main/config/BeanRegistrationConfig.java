package main.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanRegistrationConfig {

  @Value("${cloudinary.url}")
  private String cloudURL;

  @Bean(name = "com.cloudinary.Cloudinary")
  public Cloudinary getCloudinary() {
    return new Cloudinary(cloudURL);
  }
}
