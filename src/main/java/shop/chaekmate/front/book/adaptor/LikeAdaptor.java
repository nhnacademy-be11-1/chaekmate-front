package shop.chaekmate.front.book.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.chaekmate.front.book.dto.response.LikeResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "like-client", url = "${chaekmate.gateway.url}")
public interface LikeAdaptor {

    @PostMapping(path="/books/{bookId}/likes")
    CommonResponse<LikeResponse> createLike(@PathVariable Long bookId);

    @DeleteMapping(path="/books/{bookId}/likes")
    CommonResponse<Void> deleteLike(@PathVariable Long bookId);

    @GetMapping(path="/members/likes")
    CommonResponse<List<LikeResponse>> getMemberLikes();

}
