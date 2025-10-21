package shop.chaekmate.front.dto;

import java.time.LocalDate;

public record Coupon(String code, String description, double discountAmount, LocalDate expiryDate) {}
