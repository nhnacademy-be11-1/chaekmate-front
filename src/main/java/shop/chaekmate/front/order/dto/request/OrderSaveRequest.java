package shop.chaekmate.front.order.dto.request;

import java.util.List;

public record OrderSaveRequest(
        String ordererName,
        String ordererPhone,
        String ordererEmail,

        String recipientName,
        String recipientPhone,

        String zipcode,
        String streetName,
        String detail,

        String deliveryRequest,
        String deliveryAt,   // LocalDate or String (ISO date)

        Integer deliveryFee,
        Integer totalPrice,

        List<OrderedBookSaveRequest> orderedBooks
) {}
