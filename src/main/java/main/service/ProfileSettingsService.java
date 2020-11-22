package main.service;

import java.io.IOException;
import java.util.TreeMap;
import main.api.request.ProfileSettingsRequest;
import main.api.response.ResultErrorsResponse;
import main.data.UploadType;
import main.model.User;
import main.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  public ResponseEntity<?> editProfile(ProfileSettingsRequest request, String email) {
    TreeMap<String, String> errorsMap = new TreeMap<>();

    if (usersRepository.existsByEmail(email)) {
      User user = usersRepository.findByEmail(email);
      if (request.getPhoto() != null) {
        if (request.getPhoto().toString().isBlank() && request.isRemovePhoto()) {
          user.setPhoto("");
        } else {
          try {
            user.setPhoto(
                storageService.saveImage((MultipartFile) request.getPhoto(), UploadType.AVATAR));
          } catch (IOException ex) {
            errorsMap.put("photo", ex.getLocalizedMessage());
          }
        }
      }
      if (request.getEmail() != null) {
        if (usersRepository.existsByEmail(request.getEmail())
            && !request.getEmail().equals(email)) {
          errorsMap.put("email", "Этот e-mail уже зарегистрирован");
        }
        user.setEmail(request.getEmail());
      }
      if (request.getName() != null) {
        if (request.getName().toLowerCase().contains("admin")
            || request.getName().trim().length() < 4) {
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
      if (errorsMap.isEmpty()) {
        usersRepository.save(user);
        return new ResponseEntity<>(ResultErrorsResponse
            .builder()
            .result(true)
            .build(), HttpStatus.OK);
      }
    }
    return new ResponseEntity<>(ResultErrorsResponse
        .builder()
        .result(false)
        .errors(errorsMap)
        .build(), HttpStatus.BAD_REQUEST);
  }
}
