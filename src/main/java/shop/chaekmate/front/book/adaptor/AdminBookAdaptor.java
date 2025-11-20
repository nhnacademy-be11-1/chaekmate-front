package shop.chaekmate.front.book.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.request.BookModifyRequest;
import shop.chaekmate.front.book.dto.response.AdminBookCreateResponse;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.book.dto.response.AladinBookResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "admin-book-adaptor", url = "${chaekmate.gateway.url}")
public interface AdminBookAdaptor {

    // 최근 추가된 도서 조회
    @GetMapping("/admin/books/recent")
    CommonResponse<List<AdminBookResponse>> getBooks(@RequestParam int limit);

    // 관리자용 도서 목록 조회
    @GetMapping("/admin/books/paged")
    CommonResponse<Page<AdminBookResponse>> getAdminBooksPaged(@RequestParam int page,
                                                               @RequestParam int size,
                                                               @RequestParam String sortType,
                                                               @RequestParam(required = false) String keyword);

    // 특정 도서 상세 조회
    @GetMapping("/books/{bookId}")
    CommonResponse<AdminBookResponse> getBookById(@PathVariable(value = "bookId") Long bookId );

    // 도서 수정 요청
    @PutMapping("/books/{bookId}")
    CommonResponse<Void> modifyBookById(@PathVariable(value = "bookId") Long bookId, @RequestBody BookModifyRequest request);

    // 도서 생성 요청
    @PostMapping("/books")
    CommonResponse<AdminBookCreateResponse> createBook(@RequestBody BookCreateRequest bookCreateRequest);

    // 도서 삭제 요청
    @DeleteMapping("/books/{bookId}")
    CommonResponse<Void> deleteBookById(@PathVariable(value = "bookId") Long bookId);

    // 알라딘 도서 검색 요청
    @GetMapping("/admin/books/aladin/search")
    CommonResponse<Page<AladinBookResponse>> getBooksByAladinSearch(
            @RequestParam("query") String query,
            @RequestParam(value="searchType", required=false) String searchType,
            @PageableDefault(size=5) Pageable pageable
    );

}
