package main.service;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import main.api.response.RegisterResponse;
import main.model.Users;
import main.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService { //implements UserDetailsService

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private UsersRepository userRepository;

  public RegisterResponse getResponse(Users user) { //кривовато. переделаю
    TreeMap<String, String> errorsMap = new TreeMap<>();
    if(user.getName().toLowerCase().equals("admin")) {
      errorsMap.put("name", "Имя указано неверно");
    }
    if (user.getPassword().length()<6) {
      errorsMap.put("password" , "Пароль короче 6-ти символов");
    }
    if (false) { //как добвлю капчу
      errorsMap.put("captcha", "Код с картинки введён неверно");
    }
    if (errorsMap.isEmpty()) {
      if (!saveUser(user)) {
        errorsMap.put("email", "e-mail " + user.getEmail() + " уже зарегистрирован");
        return RegisterResponse.builder().result(false).errors(errorsMap).build();
      }
      return RegisterResponse.builder().result(true).build();
    }
    else {
      return RegisterResponse.builder().result(false).errors(errorsMap).build();
    }
  }

  public Users findUserById(int userId) {
    Optional<Users> userFromDb = userRepository.findById(userId);
    return userFromDb.orElse(new Users());
  }

  public List<Users> allUsers() {
    return userRepository.findAll();
  }

  public boolean saveUser(Users user) {
    Users userFromDB = userRepository.findByEmail(user.getEmail());

    if (userFromDB != null) {
      return false;
    }

    user.setPassword(user.getPassword()); //место для шифрования
    userRepository.save(user);
    return true;
  }

  public boolean deleteUser(int userId) {
    if (userRepository.findById(userId).isPresent()) {
      userRepository.deleteById(userId);
      return true;
    }
    return false;
  }

  public List<Users> usergtList(int idMin) {
    return em.createQuery("SELECT u FROM Users u WHERE u.id > :paramId", Users.class)
        .setParameter("paramId", idMin).getResultList();
  }
}
