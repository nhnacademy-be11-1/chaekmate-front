package shop.chaekmate.front.common;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CoreClient coreClient;

    // 렌더링 시 모든 페이지가 가지는 값 @ModelAttribute 로 추가 가능

}
