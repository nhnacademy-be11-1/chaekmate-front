package shop.chaekmate.front.common;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class MinioService {
    private final MinioClient minioClient;
    private final String bucketName;
    private final String chaekmateImageUrl;

    public MinioService(MinioProperties minioProperties, MinioClient minioClient) {
        this.minioClient = minioClient;
        this.bucketName = minioProperties.getBucketName();
        this.chaekmateImageUrl = minioProperties.getChaekmateImageUrl();
    }

    public String uploadFile(MultipartFile file) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();
        String objectName = UUID.randomUUID() + "-" + originalFilename; // Generate unique object name

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        return String.format("%s/%s/%s", chaekmateImageUrl, bucketName, objectName); // Manually construct URL
    }
}
