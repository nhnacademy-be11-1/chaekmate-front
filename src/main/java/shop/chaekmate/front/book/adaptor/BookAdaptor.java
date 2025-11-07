package shop.chaekmate.front.book.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.book.dto.BookListResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "book-client", url = "${chaekmate.gateway.url}")
public interface BookAdaptor {

    @GetMapping("/books")
    CommonResponse<Page<BookListResponse>> getBooksByCategory(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    );

    @GetMapping("/books/{bookId}")
    CommonResponse<BookDetailResponse> getBookById(
            @PathVariable Long bookId
    );
}
