package shop.chaekmate.front.review.service;

import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.BookAdaptor;
import shop.chaekmate.front.book.dto.BookDetailResponse;
import shop.chaekmate.front.review.adaptor.ReviewAddAdaptor;
import shop.chaekmate.front.review.dto.request.ReviewCreateRequest;
import shop.chaekmate.front.review.dto.request.ReviewImageAddRequest;
import shop.chaekmate.front.review.dto.response.ReviewCreateResponse;
import shop.chaekmate.front.review.dto.response.ReviewImageResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewAddService {

    private final BookAdaptor bookAdaptor;
    private final ReviewAddAdaptor reviewAddAdaptor;

    public BookDetailResponse getBookById(Long bookId){
        BookDetailResponse response;
        try {
            response = bookAdaptor.getBookById(bookId).data();
        } catch (FeignException e) {
            log.warn("책 정보를 가져오는 중 에러가 발생했습니다. {}",e.getMessage());
            response = null;
        }
        return response;
    }

    public ReviewCreateResponse createReview(ReviewCreateRequest request){
        ReviewCreateResponse response;
        try{
            response = reviewAddAdaptor.createReview(request).data();
        } catch (FeignException e){
            log.warn("리뷰 등록 중 에러가 발생했습니다. {}",e.getMessage());
            response = null;
        }
        return response;
    }

    public List<ReviewImageResponse> addReviewImages(Long reviewId, List<String> imageUrls){

        ReviewImageAddRequest request = new ReviewImageAddRequest();
        request.setImageUrls(imageUrls);
        List<ReviewImageResponse> response;

        try {
            response = reviewAddAdaptor.addReviewImages(reviewId, request).data();
        } catch (FeignException e){
            log.warn("리뷰 이미지 등록 중 에러 발생. {}", e.getMessage());
            response = null;
        }

        return response;
    }

}
