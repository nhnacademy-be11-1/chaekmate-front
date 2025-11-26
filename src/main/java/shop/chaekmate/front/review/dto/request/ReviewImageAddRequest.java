package shop.chaekmate.front.review.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class ReviewImageAddRequest {

    private List<String> imageUrls;
}
