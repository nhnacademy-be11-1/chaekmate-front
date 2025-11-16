package shop.chaekmate.front.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.AdminBookAdaptor;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.request.BookCreationRequest;
import shop.chaekmate.front.book.dto.request.BookModificationRequest;
import shop.chaekmate.front.book.dto.request.BookModifyRequest;
import shop.chaekmate.front.book.dto.response.AdminBookCreateResponse;
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
    private final BookImageService bookImageService;

    // 최근 추가된 도서 조회
    public List<AdminBookResponse> getRecentCreatedBooks(int limit){
        CommonResponse<List<AdminBookResponse>> wrappedResponse = adminBookAdaptor.getBooks(limit);

        return wrappedResponse.data();
    }

    // 관리자 도서 목록 조회
    public Page<AdminBookResponse> getAdminBookPaged(Pageable pageable, String sortType, String keyword){

        CommonResponse<Page<AdminBookResponse>> wrappedResponse = adminBookAdaptor.getAdminBooksPaged(pageable.getPageNumber(),pageable.getPageSize(),sortType,keyword);

        return wrappedResponse.data();
    }

    // 도서 상세 조회 메소드
    public AdminBookDetail getBookById(Long bookId){
        CommonResponse<AdminBookResponse> wrappedResponse = adminBookAdaptor.getBookById(bookId);

        return AdminBookDetail.of(wrappedResponse.data(), categoryService, tagService);
    }

    // 도서 추가
    public void createBook(BookCreationRequest request){


        // 도서 엔티티 추가
        CommonResponse<AdminBookCreateResponse> response = adminBookAdaptor.createBook(BookCreateRequest.of(request));
        Long bookId = response.data().id();

        // 섬네일 이미지 추가
        if(request.getThumbnailUrl() != null && !request.getThumbnailUrl().trim().isBlank()) {
            bookImageService.createBookThumbnail(bookId, request.getThumbnailUrl());
        }

        // 상세 이미지 추가
        if(request.getNewDetailImageUrls() != null && !request.getNewDetailImageUrls().isEmpty()){
            request.getNewDetailImageUrls().forEach(imageUrl -> bookImageService.addBookImage(bookId, imageUrl));
        }

    }

    // 도서 수정
    public void modifyBookByRequest(Long bookId , BookModificationRequest modificationRequest){
        // 도서 엔티티 수정 요청
        BookModifyRequest modifyRequest = BookModifyRequest.of(modificationRequest);
        adminBookAdaptor.modifyBookById(bookId, modifyRequest);

        // 도서 썸네일 수정
        if(modificationRequest.newThumbnailUrl() !=  null && !modificationRequest.newThumbnailUrl().isBlank()) {
            bookImageService.updateBookThumbnail(bookId, modificationRequest.newThumbnailUrl());
        }

        // 도서 상세 이미지 추가
        if(modificationRequest.newDetailImageUrls() != null && !modificationRequest.newDetailImageUrls().isEmpty()) {
            modificationRequest.newDetailImageUrls()
                    .forEach(imageUrl -> bookImageService.addBookImage(bookId, imageUrl));
        }

        // 도서 상세 이미지 삭제
        if(modificationRequest.deletedImageIds() != null && !modificationRequest.deletedImageIds().isEmpty()){
            modificationRequest.deletedImageIds().forEach(imageId -> bookImageService.deleteBookImage(bookId, imageId));
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
