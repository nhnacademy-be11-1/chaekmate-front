package shop.chaekmate.front.order.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderHistoryResponse {

    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String ordererName;
    private String ordererPhone;
    private String ordererEmail;

    private String recipientName;
    private String recipientPhone;

    private String zipcode;
    private String streetName;
    private String detail;
    private String deliveryRequest;

    private LocalDate deliveryAt;

    private Integer deliveryFee;
    private Long totalPrice;

    private String status;
    private LocalDateTime createdAt;


    private List<OrderedBookHistoryResponse> orderedBooks;
}
