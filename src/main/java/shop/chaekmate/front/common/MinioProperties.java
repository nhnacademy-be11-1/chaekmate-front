package shop.chaekmate.front.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "minio") // ← minio.* 전체 매핑
public class MinioProperties {
    private String url;
    private String bucketName;
    private String accessKey;
    private String secretKey;
}
