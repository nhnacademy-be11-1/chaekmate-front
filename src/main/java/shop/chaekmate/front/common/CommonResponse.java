package shop.chaekmate.front.common;

import java.time.LocalDateTime;

public record CommonResponse<T>(LocalDateTime timestamp, String code, T data) {
}
