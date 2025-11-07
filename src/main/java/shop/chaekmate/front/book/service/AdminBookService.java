package shop.chaekmate.front.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.AdminBookAdaptor;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.request.BookModifyRequest;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.book.dto.response.AdminBookDetail;
import shop.chaekmate.front.category.service.CategoryService;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.tag.service.TagService;

@Service
@RequiredArgsConstructor
public class AdminBookService {

    private final AdminBookAdaptor adminBookAdaptor;
    private final CategoryService categoryService;
    private final TagService tagService;

    public List<AdminBookResponse> getRecentCreatedBooks(int limit){
        CommonResponse<List<AdminBookResponse>> wrappedResponse = adminBookAdaptor.getBooks(limit);

        return wrappedResponse.data();
    }

    // 도서 상세 조회 메소드
    public AdminBookDetail getBookById(Long bookId){
        CommonResponse<AdminBookResponse> wrappedResponse = adminBookAdaptor.getBookById(bookId);

        return AdminBookDetail.of(wrappedResponse.data(), categoryService, tagService);
    }

    // 도서 추가
    public void createBook(BookCreateRequest request){

    }

    // 도서 수정
    public void modifyBookByBookDetail(AdminBookDetail adminBookDetail){

        BookModifyRequest request = BookModifyRequest.of(adminBookDetail);

        adminBookAdaptor.modifyBookById(adminBookDetail.id(), request);

    }

}
