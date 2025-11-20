package shop.chaekmate.front.point.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.point.dto.response.MemberPointHistoryResponse;
import shop.chaekmate.front.point.dto.response.PointHistoryResponse;
import shop.chaekmate.front.point.dto.response.PointResponse;

@FeignClient(name = "point-history-client", url = "${chaekmate.gateway.url}")
public interface PointHistoryAdaptor {

    @GetMapping(value = "/admin/point-histories")
    CommonResponse<Page<PointHistoryResponse>> getAllPointHistory(@SpringQueryMap Pageable pageable);

    @GetMapping(value = "/members/{memberId}/point-histories")
    CommonResponse<Page<MemberPointHistoryResponse>> getMemberPointHistory(
            @PathVariable("memberId") Long memberId,
            @SpringQueryMap Pageable pageable);

    @GetMapping(value = "/members/{memberId}/points")
    CommonResponse<PointResponse> getMemberPoint(@PathVariable("memberId") Long memberId);

}
