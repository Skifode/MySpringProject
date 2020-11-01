package main.services;

import java.util.Objects;
import java.util.TreeMap;
import main.api.request.ProfileSettingsRequest;
import main.api.response.RegisterResponse;
import main.model.User;
import main.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileSettingsService {

  private final UsersRepository usersRepository;
  private final StorageService storageService;

  @Autowired
  public ProfileSettingsService(UsersRepository usersRepository,
      StorageService storageService) {
    this.usersRepository = usersRepository;
    this.storageService = storageService;
  }

  public RegisterResponse editProfile(ProfileSettingsRequest request, String email) {
    TreeMap<String, String> errorsMap = new TreeMap<>();

    try {
      if (usersRepository.existsByEmail(email)) {
        User user = usersRepository.findByEmail(email);

        if (request.getPhoto() != null) {
          if (request.getPhoto().toString().isBlank()) {
            user.setPhoto("");
          } else if (((MultipartFile) request.getPhoto()).getBytes().length < 5242880 &&
              Objects.requireNonNull(((MultipartFile)
                  request.getPhoto())
                  .getContentType())
                  .contains("image")) {
            user.setPhoto(storageService.saveAvatar((MultipartFile) request.getPhoto()));
          } else {
            errorsMap.put("photo", "Загрузите фотографию в формате JPG, BMP, JPEG, WBMP, PNG, GIF"
                + " с размером не более 5 Мб");
          }
        }
        if (request.getEmail() != null) {
          if (usersRepository.existsByEmail(request.getEmail())) {
            errorsMap.put("email", "Этот e-mail уже зарегистрирован");
          } else {
            user.setEmail(request.getEmail());
          }
        }
        if (request.getName() != null) {
          if (request.getName().toLowerCase().contains("admin")) {
            errorsMap.put("name", "Имя указано неверно");
          } else {
            user.setName(request.getName());
          }
        }
        if (request.getPassword() != null) {
          if (request.getPassword().length() < 6) {
            errorsMap.put("password", "Пароль короче 6-ти символов");
          } else {
            user.setPassword(new BCryptPasswordEncoder(12).encode(request.getPassword()));
          }
        }
        if (request.isRemovePhoto()) {
          user.setPhoto("");
        }
        if (errorsMap.isEmpty()) {
          usersRepository.save(user);
          return RegisterResponse.builder().result(true).build();
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return RegisterResponse.builder().result(false).errors(errorsMap).build();
  }
}
