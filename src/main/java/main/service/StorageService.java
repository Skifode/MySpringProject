package main.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

  @Value("${upload.avatars}")
  private String AVATARS_UPLOAD_DIRECTORY;

  final static int TARGET_WIDTH = 36;
  final static int TARGET_HEIGHT = 36;

  public String saveAvatar(MultipartFile image) {
    String filePath = AVATARS_UPLOAD_DIRECTORY + getRandomPath() + image.getOriginalFilename();

    if (new File(filePath).mkdirs() || !new File(filePath).exists()) {
      try {
        ImageIO.write(
            resizeImage(ImageIO.read(image.getInputStream())),
            Objects.requireNonNull(image.getContentType()).split("/")[1], // ex: image/jpg
            new File(filePath));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    } else {
      return saveAvatar(image);
    }
    return filePath.split("/images")[1];
  }

  private String getRandomPath() {
    String[] randomPath = UUID.randomUUID().toString().split("-");
    return String.format("/%s" + "/%s" + "/%s/",
        randomPath[1], randomPath[2], randomPath[3]);
  }

  private BufferedImage resizeImage(
      BufferedImage originalImage) {
    return Scalr.resize(
        originalImage, Method.ULTRA_QUALITY, Mode.AUTOMATIC,
        TARGET_WIDTH, TARGET_HEIGHT, Scalr.OP_ANTIALIAS);
  }
}
