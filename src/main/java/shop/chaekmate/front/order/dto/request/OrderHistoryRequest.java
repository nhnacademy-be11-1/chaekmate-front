package shop.chaekmate.front.order.dto.request;

import lombok.Data;

@Data
public class OrderHistoryRequest {

    private String orderNumber;
    private String ordererName;
    private String ordererPhone;
}
