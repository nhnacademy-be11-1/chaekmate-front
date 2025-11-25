package shop.chaekmate.front.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeignErrorHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignStatusException(FeignException e) {

        String body = e.contentUTF8(); // Core의 JSON 형태

        if (body == null || body.isBlank()) {
            return ResponseEntity.status(e.status())
                    .body(Map.of(
                            "status", e.status(),
                            "message", "서버 오류가 발생했습니다."
                    ));
        }

        // 이미 JSON 형태이므로 그대로 반환 가능
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(body, Map.class);

            return ResponseEntity.status(e.status()).body(json);

        } catch (Exception parseEx) {
            // JSON으로 파싱이 안 되는 경우
            return ResponseEntity.status(e.status())
                    .body(Map.of(
                            "status", e.status(),
                            "message", body
                    ));
        }
    }
}
