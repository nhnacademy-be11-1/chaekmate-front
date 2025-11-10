package shop.chaekmate.front.book.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.request.BookModifyRequest;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "admin-book-adaptor", url = "${chaekmate.gateway.url}")
public interface AdminBookAdaptor {

    // 최근 추가된 도서 조회
    @GetMapping("/admin/books/recent")
    CommonResponse<List<AdminBookResponse>> getBooks(@RequestParam int limit);

    // 특정 도서 상세 조회
    @GetMapping("/books/{bookId}")
    CommonResponse<AdminBookResponse> getBookById(@PathVariable(value = "bookId") Long bookId );

    // 도서 수정 요청
    @PutMapping("/books/{bookId}")
    CommonResponse<Void> modifyBookById(@PathVariable(value = "bookId") Long bookId, @RequestBody BookModifyRequest request);

    // 도서 생성 요청
    @PostMapping("/books")
    CommonResponse<Void> createBook(@RequestBody BookCreateRequest bookCreateRequest);

    @DeleteMapping("/books/{bookId}")
    CommonResponse<Void> deleteBookById(@PathVariable(value = "bookId") Long bookId);

}
