package shop.chaekmate.front.point.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointPolicyUpdateRequest {
    private String type;
    private int point;
}