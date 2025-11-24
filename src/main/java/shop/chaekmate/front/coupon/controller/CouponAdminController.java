package shop.chaekmate.front.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.chaekmate.front.category.adaptor.CategoryAdaptor;
import shop.chaekmate.front.category.dto.response.CategoryResponse;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.coupon.adaptor.CouponAdminAdaptor;
import shop.chaekmate.front.coupon.dto.request.CouponPolicyCreateRequest;
import shop.chaekmate.front.coupon.dto.request.CouponPolicyUpdateRequest;
import shop.chaekmate.front.coupon.dto.response.CouponPoliciesGetResponse;
import shop.chaekmate.front.coupon.dto.response.CouponPolicyGetResponse;
import shop.chaekmate.front.coupon.type.CouponAppliedPeriodType;
import shop.chaekmate.front.coupon.type.CouponType;
import shop.chaekmate.front.coupon.type.DiscountType;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
public class CouponAdminController {

    private final CouponAdminAdaptor couponAdminAdaptor;
    private final CategoryAdaptor categoryAdaptor;

    /**
     * 쿠폰 목록 페이지
     */
    @GetMapping
    public String getCouponList(Model model) {
        CommonResponse<List<CouponPoliciesGetResponse>> response =
                couponAdminAdaptor.getCouponPolicies();

        model.addAttribute("coupons", response.data());
        model.addAttribute("title", "쿠폰 관리");

        return "admin/coupon/coupon-list-management";
    }

    /**
     * 쿠폰 상세 페이지
     */
    @GetMapping("/{id}")
    public String getCouponDetail(@PathVariable Long id, Model model) {
        CommonResponse<CouponPolicyGetResponse> response =
                couponAdminAdaptor.getCouponPolicyById(id);

        model.addAttribute("coupon", response.data());
        model.addAttribute("title", "쿠폰 상세");

        return "admin/coupon/coupon-detail-management";
    }

    /**
     * 쿠폰 생성 폼 페이지
     */
    @GetMapping("/new")
    public String createCouponForm(Model model) {
        // Enum 목록
        model.addAttribute("couponTypes", CouponType.values());
        model.addAttribute("discountTypes", DiscountType.values());
        model.addAttribute("periodTypes", CouponAppliedPeriodType.values());

        // 카테고리 트리
        CommonResponse<List<CategoryResponse>> categoryResponse =
                categoryAdaptor.getAllCategories();
        model.addAttribute("categories", categoryResponse.data());

        model.addAttribute("title", "쿠폰 생성");

        return "admin/coupon/coupon-form-management";
    }

    /**
     * 쿠폰 생성 처리
     */
    @PostMapping
    public String createCoupon(
            @RequestParam String name,
            @RequestParam CouponType type,
            @RequestParam CouponAppliedPeriodType appliedPeriodType,
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) LocalDateTime appliedStartedAt,
            @RequestParam(required = false) LocalDateTime appliedExpiredAt,
            @RequestParam DiscountType discountType,
            @RequestParam int discountValue,
            @RequestParam Integer minAvailableAmount,
            @RequestParam(required = false) Long maxAppliedAmount,
            @RequestParam(required = false) Long remainingQuantity,
            RedirectAttributes redirectAttributes) {

        CouponPolicyCreateRequest request = new CouponPolicyCreateRequest(
                name,
                type,
                appliedPeriodType,
                ids,
                appliedStartedAt,
                appliedExpiredAt,
                discountType,
                discountValue,
                minAvailableAmount,
                maxAppliedAmount,
                remainingQuantity
        );

        couponAdminAdaptor.createCouponPolicy(request);

        redirectAttributes.addFlashAttribute("message", "쿠폰이 생성되었습니다.");
        return "redirect:/admin/coupons";
    }

    /**
     * 쿠폰 수정 폼 페이지
     */
    @GetMapping("/{id}/edit")
    public String editCouponForm(@PathVariable Long id, Model model) {
        CommonResponse<CouponPolicyGetResponse> response =
                couponAdminAdaptor.getCouponPolicyById(id);

        model.addAttribute("coupon", response.data());
        model.addAttribute("couponTypes", CouponType.values());
        model.addAttribute("discountTypes", DiscountType.values());
        model.addAttribute("periodTypes", CouponAppliedPeriodType.values());

        // 카테고리 트리
        CommonResponse<List<CategoryResponse>> categoryResponse =
                categoryAdaptor.getAllCategories();
        model.addAttribute("categories", categoryResponse.data());

        model.addAttribute("title", "쿠폰 수정");

        return "admin/coupon/coupon-form-management";
    }

    /**
     * 쿠폰 수정 처리
     */
    @PostMapping("/{id}")
    public String updateCoupon(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam CouponType type,
            @RequestParam CouponAppliedPeriodType appliedPeriodType,
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) LocalDateTime appliedStartedAt,
            @RequestParam(required = false) LocalDateTime appliedExpiredAt,
            @RequestParam DiscountType discountType,
            @RequestParam int discountValue,
            @RequestParam Integer minAvailableAmount,
            @RequestParam(required = false) Long maxAppliedAmount,
            @RequestParam(required = false) Long remainingQuantity,
            RedirectAttributes redirectAttributes) {

        CouponPolicyUpdateRequest request = new CouponPolicyUpdateRequest(
                name,
                type,
                appliedPeriodType,
                ids,
                appliedStartedAt,
                appliedExpiredAt,
                discountType,
                discountValue,
                minAvailableAmount,
                maxAppliedAmount,
                remainingQuantity
        );

        couponAdminAdaptor.updateCouponPolicy(id, request);

        redirectAttributes.addFlashAttribute("message", "쿠폰이 수정되었습니다.");
        return "redirect:/admin/coupons/" + id;
    }

    /**
     * 쿠폰 삭제 처리
     */
    @PostMapping("/{id}/delete")
    public String deleteCoupon(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        couponAdminAdaptor.deleteCouponPolicy(id);

        redirectAttributes.addFlashAttribute("message", "쿠폰이 삭제되었습니다.");
        return "redirect:/admin/coupons";
    }
}
