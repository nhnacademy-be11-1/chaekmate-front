package shop.chaekmate.front.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.AdminBookAdaptor;
import shop.chaekmate.front.book.adaptor.BookAdaptor;
import shop.chaekmate.front.book.adaptor.BookImageAdaptor;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.request.BookImageAddRequest;
import shop.chaekmate.front.book.dto.request.BookModificationRequest;
import shop.chaekmate.front.book.dto.request.BookModifyRequest;
import shop.chaekmate.front.book.dto.request.BookThumbnailUpdateRequest;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.book.dto.response.AdminBookDetail;
import shop.chaekmate.front.book.dto.response.AladinBookResponse;
import shop.chaekmate.front.category.service.CategoryService;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.tag.service.TagService;

@Service
@RequiredArgsConstructor
public class AdminBookService {

    private final AdminBookAdaptor adminBookAdaptor;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final BookImageAdaptor bookImageAdaptor;

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

        adminBookAdaptor.createBook(request);

    }

    // 도서 수정
    public void modifyBookByRequest(Long bookId , BookModificationRequest modificationRequest){
        // 도서 엔티티 수정 요청
        BookModifyRequest modifyRequest = BookModifyRequest.of(modificationRequest);
        adminBookAdaptor.modifyBookById(bookId, modifyRequest);

        // 도서 썸네일 수정
        if(modificationRequest.newThumbnailUrl() !=  null && !modificationRequest.newThumbnailUrl().isBlank()) {
            BookThumbnailUpdateRequest thumbnailUpdateRequest = new BookThumbnailUpdateRequest(
                    modificationRequest.newThumbnailUrl());
            bookImageAdaptor.updateBookThumbnail(bookId, thumbnailUpdateRequest);
        }

        // 도서 상세 이미지 추가
        if(modificationRequest.newDetailImageUrls() != null && !modificationRequest.newDetailImageUrls().isEmpty()) {
            modificationRequest.newDetailImageUrls()
                    .forEach(imageUrl -> bookImageAdaptor.addBookImage(bookId, new BookImageAddRequest(imageUrl)));
        }

        // 도서 상세 이미지 삭제
        if(modificationRequest.deletedImageIds() != null && !modificationRequest.deletedImageIds().isEmpty()){
            modificationRequest.deletedImageIds().forEach(imageId -> bookImageAdaptor.deleteBookImage(bookId, imageId));
        }
    }

    // 도서 삭제
    public void deleteBookByBookId(Long bookId){

        adminBookAdaptor.deleteBookById(bookId);

    }

    // 알라딘으로 도서 검색
    public Page<AladinBookResponse> searchBooksByAladin(String query, String searchType, Pageable pageable){

        CommonResponse<Page<AladinBookResponse>> wrappedResponse = adminBookAdaptor.getBooksByAladinSearch(query, searchType, pageable);

        return wrappedResponse.data();

    }

}
