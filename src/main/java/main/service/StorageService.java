package main.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import main.data.UploadType;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Component
public class StorageService {

  @Value("${upload.avatars}")
  private String avatarsUploadDirectory;

  @Value("${upload.image}")
  private String imageUploadDirectory;

  @Value("${avatar.width}")
  private int targetWidth;

  @Value("${avatar.height}")
  private int targetHeight;

  public String saveImage(MultipartFile image, UploadType type) throws IOException {

    String contentType = Objects
        .requireNonNull(image.getContentType())
        .toLowerCase()
        .split("/")[1]; // ex: image/jpg
    String path = getRandomPath() + image.getOriginalFilename();

    if (!contentType.contains("jpg") &&
        !contentType.contains("jpeg") ||
        image.getBytes().length > 5242880) {
      throw new IOException("Загрузите фотографию в формате JPG или JPEG"
          + " с размером не более 5 Мб");
    }

    String filePath = switch (type) {
      case IMAGE -> imageUploadDirectory + path;
      case AVATAR -> avatarsUploadDirectory + path;
    };
    BufferedImage bufferedImage = switch (type) {
      case AVATAR -> resizeImage(ImageIO.read(image.getInputStream()));
      case IMAGE -> ImageIO.read(image.getInputStream());
    };

    if (new File(filePath).mkdirs() || !new File(filePath).exists()) {
      ImageIO.write(bufferedImage, contentType, new File(filePath));
    } else {
      return saveImage(image, type);
    }
    return filePath.split("images")[1];
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
        targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
  }
}
