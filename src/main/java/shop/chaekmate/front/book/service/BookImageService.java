package shop.chaekmate.front.book.service;

import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.BookImageAdaptor;
import shop.chaekmate.front.book.dto.request.BookImageAddRequest;
import shop.chaekmate.front.book.dto.request.BookThumbnailUpdateRequest;
import shop.chaekmate.front.book.dto.response.BookImageResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.common.CommonResponse;

@Service
@RequiredArgsConstructor
public class BookImageService {

    private final BookImageAdaptor bookImageAdaptor;

    // 섬네일 조회
    public BookThumbnailResponse getThumbnailByBookId(Long bookId){
        CommonResponse<BookThumbnailResponse> response;
        try {
            response = bookImageAdaptor.getBookThumbnail(bookId);
        } catch (FeignException e){
            return null;
        }
        return response.data();
    }


    // 상세 이미지 조회
    public List<BookImageResponse> getDetailImagesByBookId(Long bookId){
        CommonResponse<List<BookImageResponse>> response = bookImageAdaptor.getBookDetailImages(bookId);

        return response.data();
    }

    // 섬네일 생성
    public void createBookThumbnail(Long bookId, String thumbnailUrl) {
        // 먼저 들어간것이 섬네일이 된다
        bookImageAdaptor.addBookImage(bookId, new BookImageAddRequest(thumbnailUrl));
    }

    // 섬네일 수정
    public void updateBookThumbnail(Long bookId, String thumbnailUrl){
        bookImageAdaptor.updateBookThumbnail(bookId,new BookThumbnailUpdateRequest(thumbnailUrl));
    }

    // 도서 이미지 추가
    public void addBookImage(Long bookId, String imageUrl){
        bookImageAdaptor.addBookImage(bookId, new BookImageAddRequest(imageUrl));
    }

    // 도서 상세 이미지 삭제
    public void deleteBookImage(Long bookId, Long bookImageId){
        bookImageAdaptor.deleteBookImage(bookId, bookImageId);
    }

}
