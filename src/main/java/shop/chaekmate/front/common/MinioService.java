package shop.chaekmate.front.common;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class MinioService {
    private final MinioClient minioClient;

    private final String minioUrl;
    private final String bucketName;

    public MinioService(MinioProperties minioProperties, MinioClient minioClient) {
        this.minioClient = minioClient;
        this.minioUrl = minioProperties.getUrl();
        this.bucketName = minioProperties.getBucketName();
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

        return String.format("%s/%s/%s", minioUrl, bucketName, objectName); // Manually construct URL
    }
}
