package shop.chaekmate.front.book.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.book.dto.request.BookImageAddRequest;
import shop.chaekmate.front.book.dto.request.BookThumbnailUpdateRequest;
import shop.chaekmate.front.book.dto.response.BookImageResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "bookImage-client", url = "${chaekmate.gateway.url}")
public interface BookImageAdaptor {

    // 도서 썸네일 조회
    @GetMapping("/books/{bookId}/images/thumbnail")
    CommonResponse<BookThumbnailResponse> getBookThumbnail(@PathVariable Long bookId);

    // 도서 썸네일 수정
    @PutMapping("/books/{bookId}/images/thumbnail")
    CommonResponse<Void> updateBookThumbnail(@PathVariable Long bookId,
                                             @RequestBody BookThumbnailUpdateRequest request);

    // 도서 모든 이미지 조회
    @GetMapping("/books/{bookId}/images")
    CommonResponse<List<BookImageResponse>> getBookAllImages(@PathVariable Long bookId);

    // 특정 도서에 이미지 하나 추가
    @PostMapping("/books/{bookId}/images")
    CommonResponse<BookImageResponse> addBookImage(@PathVariable Long bookId,
                                                   @RequestBody BookImageAddRequest request);

    // 도서 상세 이미지 목록 조회 (썸네일 제외)
    @GetMapping("/books/{bookId}/images/details")
    CommonResponse<List<BookImageResponse>> getBookDetailImages(@PathVariable Long bookId);

    // 특정 도서 특정 이미지 삭제
    @DeleteMapping("/books/{bookId}/images/{imageId}")
    CommonResponse<Void> deleteBookImage(@PathVariable Long bookId,
                                         @PathVariable Long imageId);

}
