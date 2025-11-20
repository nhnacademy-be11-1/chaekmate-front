package shop.chaekmate.front.coupon.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.coupon.dto.request.CouponPolicyCreateRequest;
import shop.chaekmate.front.coupon.dto.request.CouponPolicyUpdateRequest;
import shop.chaekmate.front.coupon.dto.response.CouponPoliciesGetResponse;
import shop.chaekmate.front.coupon.dto.response.CouponPolicyGetResponse;

import java.util.List;

@FeignClient(name = "coupon-admin-client", url = "${chaekmate.gateway.url}")
public interface CouponAdminAdaptor {

    /**
     * 쿠폰 정책 목록 조회
     */
    @GetMapping("/admin/coupon-policies")
    CommonResponse<List<CouponPoliciesGetResponse>> getCouponPolicies();

    /**
     * 쿠폰 정책 상세 조회
     */
    @GetMapping("/admin/coupon-policies/{id}")
    CommonResponse<CouponPolicyGetResponse> getCouponPolicyById(
            @PathVariable("id") Long id
    );

    /**
     * 쿠폰 정책 생성
     */
    @PostMapping("/admin/coupon-policies")
    CommonResponse<Void> createCouponPolicy(
            @RequestBody CouponPolicyCreateRequest request
    );

    /**
     * 쿠폰 정책 수정
     */
    @PatchMapping("/admin/coupon-policies/{id}")
    CommonResponse<Void> updateCouponPolicy(
            @PathVariable("id") Long id,
            @RequestBody CouponPolicyUpdateRequest request
    );

    /**
     * 쿠폰 정책 삭제
     */
    @DeleteMapping("/admin/coupon-policies/{id}")
    CommonResponse<Void> deleteCouponPolicy(
            @PathVariable("id") Long id
    );
}
