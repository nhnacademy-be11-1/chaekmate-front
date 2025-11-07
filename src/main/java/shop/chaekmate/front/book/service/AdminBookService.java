package shop.chaekmate.front.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.AdminBookAdaptor;
import shop.chaekmate.front.book.dto.request.BookCreateRequest;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.common.CommonResponse;

@Service
@RequiredArgsConstructor
public class AdminBookService {

    private final AdminBookAdaptor adminBookAdaptor;

    public List<AdminBookResponse> getRecentCreatedBooks(int limit){
        CommonResponse<List<AdminBookResponse>> wrappedResponse = adminBookAdaptor.getBooks(limit);

        return wrappedResponse.data();
    }

    public void createBook(BookCreateRequest request){

    }

}
