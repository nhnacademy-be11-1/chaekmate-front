package shop.chaekmate.front.auth.util;

import org.springframework.stereotype.Component;
import shop.chaekmate.front.auth.jwt.parser.JwtMemberIdParser;
import shop.chaekmate.front.auth.jwt.validator.JwtTokenValidator;

@Component
public class JwtUtil {

    public Long getMemberIdFromToken(String token) {
        return JwtMemberIdParser.getMemberId(token);
    }

    public boolean hasToken(String token) {
        return JwtTokenValidator.hasToken(token);
    }
}