package shop.chaekmate.front.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleFeignException(FeignException e) {
        try {
            // Core 서버에서 온 JSON 파싱
            String responseBody = e.contentUTF8();
            Map<String, Object> errorMap = objectMapper.readValue(responseBody, Map.class);

            String message = (String) errorMap.get("message");

            CommonResponse<Map<String, String>> response = new CommonResponse<>(
                    LocalDateTime.now(),
                    "ERROR",
                    Map.of("message", message)
            );

            return ResponseEntity
                    .status(e.status())
                    .body(response);

        } catch (Exception ex) {
            CommonResponse<Map<String, String>> response = new CommonResponse<>(
                    LocalDateTime.now(),
                    "ERROR",
                    Map.of("message", "처리 중 오류가 발생했습니다.")
            );

            return ResponseEntity
                    .status(500)
                    .body(response);
        }
    }
}