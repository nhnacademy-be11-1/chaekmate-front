package shop.chaekmate.front.book.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class BookQuerySliceResponse {
    List<BookQueryResponse> content;
    Boolean hasNext;
}
