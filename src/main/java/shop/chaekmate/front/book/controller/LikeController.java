package shop.chaekmate.front.book.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.chaekmate.front.auth.principal.CustomPrincipal;
import shop.chaekmate.front.book.dto.request.LikeRequest;
import shop.chaekmate.front.book.service.LikeService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes/action")
    public ResponseEntity<Map<String,Object>> toggleLike(@RequestBody LikeRequest request,
                                                         @AuthenticationPrincipal CustomPrincipal principal,
                                                         HttpServletRequest httpRequest){

        if (principal == null || principal.getMemberId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }

        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            log.debug("Header: {}", name);
        }

        Long bookId = request.bookId();
        if (bookId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "bookId가 필요합니다."));
        }

        String actionType = request.actionType();

        // Service 호출: 현재 로그인 사용자 기준으로 토글
        boolean liked = likeService.toggleLike(bookId, actionType);

        // 결과 반환
        return ResponseEntity.ok(Map.of("liked", liked));
    }

}
