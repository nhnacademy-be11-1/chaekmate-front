package shop.chaekmate.front.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.BookImageAdaptor;
import shop.chaekmate.front.book.dto.response.BookImageResponse;
import shop.chaekmate.front.book.dto.response.BookThumbnailResponse;
import shop.chaekmate.front.common.CommonResponse;

@Service
@RequiredArgsConstructor
public class BookImageService {

    private final BookImageAdaptor bookImageAdaptor;

    public BookThumbnailResponse getThumbnailByBookId(Long bookId){
        CommonResponse<BookThumbnailResponse> response = bookImageAdaptor.getBookThumbnail(bookId);

        return response.data();
    }

    public List<BookImageResponse> getDetailImagesByBookId(Long bookId){
        CommonResponse<List<BookImageResponse>> response = bookImageAdaptor.getBookDetailImages(bookId);

        return response.data();
    }

}
