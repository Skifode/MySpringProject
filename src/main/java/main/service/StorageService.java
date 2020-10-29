package main.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

  //перекину в конфиг, сменю директорию и исправлю создание папок
  private static final String DEFAULT_UPLOAD_DIRECTORY =
      "src/main/resources/static/img/upload/";

  public String saveImage(MultipartFile image) {
    String[] randomPass = UUID.randomUUID().toString().split("-");
    String filePass =
        DEFAULT_UPLOAD_DIRECTORY +
            randomPass[1] + "/" +
            randomPass[2] + "/" +
            randomPass[3] + "/";

    if(new File(filePass).mkdirs()) {
      filePass = filePass + image.getOriginalFilename();

      try {
        image.transferTo(Paths.get(filePass));
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    return filePass.split("/static")[1];
  }
}
