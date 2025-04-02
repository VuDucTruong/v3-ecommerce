package shop.holy.v3.ecommerce.service.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.shared.property.CloudinaryProperties;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(CloudinaryProperties cloudinaryProperties) {
        String cloudName = cloudinaryProperties.getCloudName();
        String apiKey = cloudinaryProperties.getApiKey();
        String apiSecret = cloudinaryProperties.getApiSecret();
        cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey,
                "api_secret", apiSecret, "secure", true));
    }

    // Upload a file to a folder (including nested folders)
    public String uploadFile(MultipartFile file, String folderPath, String publicId) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folderPath,
                        "public_id", publicId,
                        "overwrite", true)); // folderPath is the full path you want to upload to
        return uploadResult.get("secure_url").toString();  // Return the uploaded file's URL
    }

    // Delete a file by public ID
    public String deleteFile(String publicId) throws IOException {
        try{
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId,
                    ObjectUtils.emptyMap());
            return deleteResult.get("result").toString();
        }
        catch (Exception e){
            return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScWo-hawnOFdwHxQcPk8k5a7Kyk94pps-fYw&s,Screenshot 2024-10-25 220247.png";
        }
    }
    public String makeUrl(String publicId) {
        return cloudinary.url().generate(publicId);
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
