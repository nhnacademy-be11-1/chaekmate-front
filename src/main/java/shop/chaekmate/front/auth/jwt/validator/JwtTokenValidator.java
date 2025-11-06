package shop.chaekmate.front.auth.jwt.validator;

public class JwtTokenValidator {

    private JwtTokenValidator() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static boolean hasToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }
}