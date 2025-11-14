package shop.chaekmate.front.point.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.point.dto.request.PointPolicyCreateRequest;
import shop.chaekmate.front.point.dto.request.PointPolicyUpdateRequest;
import shop.chaekmate.front.point.dto.response.PointPolicyResponse;

import java.util.List;

@FeignClient(name="pointpolicies-client" , url = "${chaekmate.gateway.url}")
public interface PointPolicyAdaptor {

    @GetMapping(value= "/admin/point-policies")
    CommonResponse<List<PointPolicyResponse>> getAllPointPolicy();

    @DeleteMapping(value = "/admin/point-policies/{type}")
    CommonResponse<Void> deletePointPolicy(@PathVariable("type") String type);

    @PostMapping(value = "/admin/point-policies")
    CommonResponse<PointPolicyResponse> createPointPolicy(@RequestBody PointPolicyCreateRequest request);

    @PutMapping(value = "/admin/point-policies/{type}")
    CommonResponse<Void> updatePointPolicy(@PathVariable("type") String type,
                                           @RequestBody PointPolicyUpdateRequest request);
}
