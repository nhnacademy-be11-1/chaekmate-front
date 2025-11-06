package shop.chaekmate.front.auth.jwt.parser;

import java.util.Map;

public class JwtMemberIdParser {

    private JwtMemberIdParser() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static Long getMemberId(String token) {
        Map<String, Object> claims = JwtTokenParser.parsePayload(token);
        if (claims == null) {
            return null;
        }

        String sub = (String) claims.get("sub");
        if (sub == null) {
            return null;
        }

        try {
            return Long.parseLong(sub);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
