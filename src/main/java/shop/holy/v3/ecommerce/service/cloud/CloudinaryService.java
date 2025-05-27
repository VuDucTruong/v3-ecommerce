package shop.holy.v3.ecommerce.service.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.property.CloudinaryProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Getter
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final String cloudName;

    public CloudinaryService(CloudinaryProperties cloudinaryProperties) {
        String cloudName = cloudinaryProperties.getCloudName();
        String apiKey = cloudinaryProperties.getApiKey();
        String apiSecret = cloudinaryProperties.getApiSecret();
        cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey,
                "api_secret", apiSecret, "secure", true));
        this.cloudName = cloudName;
    }

    // Upload a file to a folder (including nested folders)
    public String uploadFile(MultipartFile file, String folderPath, String publicId) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderPath,
                        "public_id", publicId,
                        "overwrite", true)); // folderPath is the full path you want to upload to
        return uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
    }

    // Delete a file by public ID
    public String deleteFile(String publicIdWithExtensions) throws IOException {
        try {
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicIdWithExtensions.split("\\.")[0],
                    ObjectUtils.emptyMap());
            return deleteResult.get("result").toString();
        } catch (Exception e) {
            return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScWo-hawnOFdwHxQcPk8k5a7Kyk94pps-fYw&s,Screenshot 2024-10-25 220247.png";
        }
    }

    public String makeUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }

    public boolean isValidCloudinaryUrl(String url) {
        String base = "https://res.cloudinary.com/" + cloudName + "/image/upload/";
        return url != null && url.startsWith(base);
    }

    public String extractPublicId(String url, String folder) {
        if(!isValidCloudinaryUrl(url))
            throw BizErrors.INVALID_IMAGE_URL.exception();

        int folderIndex = url.indexOf(folder);
        if (folderIndex == -1) return null;

        String sub = url.substring(folderIndex);
        int dotIndex = sub.lastIndexOf('.');
        return dotIndex != -1 ? sub.substring(0, dotIndex) : sub;
    }

    public String[] uploadMultipleFiles(MultipartFile[] files, String folderPath) throws IOException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch latch = new CountDownLatch(files.length);
        String[] results = new String[files.length];
        try {
            for (int i = 0; i < files.length; i++) {
                final int index = i;
                executor.submit(() -> {
                    try {
                        results[index] = uploadFile(files[index], folderPath, UUID.randomUUID().toString());
                    } catch (IOException e) {
                        results[index] = null; // Handle the exception by setting the result to null
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await(); // Wait for all tasks to complete
            return results;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("File upload interrupted", e);
        } finally {
            executor.shutdownNow();
        }
    }
}
