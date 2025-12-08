package shop.chaekmate.front.payment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import shop.chaekmate.front.payment.dto.request.CanceledBooksRequest;

public record ReturnBooksResponse(
        String orderNumber,
        long returnedCash,                 
        int returnedPoint,                
        long returnFee,                   
        LocalDateTime returnedAt,
        List<CanceledBooksRequest> returnBooks
) {}
