package main.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import main.data.UploadType;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Value("${cloudinary.url}")
  private String cloudURL;

  @Value("${avatar.width}")
  private int targetWidth;

  @Value("${avatar.height}")
  private int targetHeight;

  @Value("${max.upload.image-size.bytes}")
  private int maxUploadSize;

  private final Cloudinary cloudinary;

  @Autowired
  public StorageService(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  public String saveImage(MultipartFile image, UploadType type) throws IOException {

    byte[] imageBytes = image.getBytes();

    String path = switch (type) {
      case IMAGE -> imageUploadDirectory;
      case AVATAR -> avatarsUploadDirectory;
    } + getRandomPath();

    Map params = ObjectUtils.asMap(
        "public_id", path,
        "resource_type", "image"
    );

    try {
      if (imageBytes.length > maxUploadSize) {
        throw new RuntimeException();
      }
      cloudinary.uploader().upload(imageBytes, params);
    } catch (RuntimeException ex){
      throw new IOException("Загрузите изображение с размером не более 5 Мб");
    }

    return switch (type) {
      case IMAGE -> getCloudPath(path);
      case AVATAR -> getCloudPath2Avatars(path);
    };
  }

  public String getCloudPath(String path) {
    return cloudinary
        .url()
        .generate(path);
  }

  public String getCloudPath2Avatars(String path) {
    return cloudinary
        .url()
        .transformation(
            new Transformation<>()
                .width(targetWidth)
                .height(targetHeight))
        .generate(path);
  }

  private String getRandomPath() {
    String[] randomPath = UUID.randomUUID().toString().split("-");
    return String.format("/%s" + "/%s" + "/%s/" + "%s",
        randomPath[1], randomPath[2], randomPath[3], randomPath[0]);
  }
}
