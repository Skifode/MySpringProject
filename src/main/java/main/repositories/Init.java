package main.repositories;

import lombok.Data;

@Data
public class Init { // У вас вопрос: -а что это тут делает?

  // А у меня их целых три!
  // Это будет сервис? Закинуть в бд? Как лучше поступить?
  // Понимаю, что сейчас тут ужасно :с

  private String title;
  private String subtitle;
  private String phone;
  private String email;
  private String copyright;
  private String copyrightFrom;

  public Init() {
    title = "DevPub";
    subtitle = "Рассказы разработчиков";
    phone = "+7 903 666-44-55";
    email = "mail@mail.ru";
    copyright = "Александр Дмитриев";
    copyrightFrom = "все права не защитить. 2005";
  }
}
