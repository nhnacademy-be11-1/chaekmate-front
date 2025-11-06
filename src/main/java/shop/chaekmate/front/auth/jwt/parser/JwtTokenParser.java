package shop.chaekmate.front.auth.jwt.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;

public class JwtTokenParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JwtTokenParser() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static Map<String, Object> parsePayload(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }

            String payload = parts[1];
            byte[] decoded = Base64.getUrlDecoder().decode(payload);
            String json = new String(decoded);

            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
}
