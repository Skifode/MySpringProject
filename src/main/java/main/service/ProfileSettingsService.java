package main.service;

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

    if (usersRepository.existsByEmail(email)) {
      User user = usersRepository.findByEmail(email);

      if (request.getPhoto() != null) {
        if (request.getPhoto().toString().isBlank()) {
          user.setPhoto("");
        } else {
          user.setPhoto(storageService.saveImage((MultipartFile) request.getPhoto()));
        }
      }
      if (request.getEmail() != null) {
        user.setEmail(request.getEmail());
      }
      if (request.getName() != null) {
        user.setName(request.getName());
      }
      if (request.getPassword() != null) {
        user.setPassword(new BCryptPasswordEncoder(12).encode(request.getPassword()));
      }
      if (request.isRemovePhoto()) {
        user.setPhoto("");
      }

      usersRepository.save(user);
    }

    return RegisterResponse.builder().result(true).build();
  }
}
