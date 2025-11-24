package shop.chaekmate.front.order.dto.response;

import lombok.Data;

@Data
public class OrderedBookHistoryResponse {
    private Long bookId;
    private String bookTitle;
    private Integer quantity;
    private Integer finalUnitPrice;
}
