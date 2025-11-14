package shop.chaekmate.front.common;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    private final String minioUrl;
    private final String minioAccessKey;
    private final String minioSecretKey;
    private final String bucket;

    public MinioConfig(MinioProperties minioProperties) {
        this.minioUrl = minioProperties.getUrl();
        this.minioAccessKey = minioProperties.getAccessKey();
        this.minioSecretKey = minioProperties.getSecretKey();
        this.bucket = minioProperties.getBucketName();
    }

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioSecretKey)
                .build();

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            log.warn("bucketError found : {}", e.getMessage(), e);
        }

        return minioClient;
    }
}
