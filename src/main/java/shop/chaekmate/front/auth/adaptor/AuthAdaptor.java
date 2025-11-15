package shop.chaekmate.front.auth.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.auth.config.AuthFeignClientConfig;
import shop.chaekmate.front.auth.dto.request.LoginRequest;
import shop.chaekmate.front.auth.dto.response.LoginResponse;
import shop.chaekmate.front.auth.dto.response.LogoutResponse;
import shop.chaekmate.front.auth.dto.response.MemberInfoResponse;

@FeignClient(name = "auth-client", url = "${chaekmate.gateway.url}", configuration = AuthFeignClientConfig.class)
public interface AuthAdaptor {

    @PostMapping("/auth/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);

    @PostMapping("/auth/admin/login")
    ResponseEntity<LoginResponse> adminLogin(@RequestBody LoginRequest request);

    @GetMapping("/auth/me")
    ResponseEntity<MemberInfoResponse> getMemberInfo(
            @CookieValue("accessToken") String token
    );

    @PostMapping("/auth/logout")
    ResponseEntity<LogoutResponse> logout(@CookieValue(value= "accessToken", required = false) String accessToken,
                                          @CookieValue(value = "refreshToken", required = false) String refreshToken);

    @PostMapping("/auth/refresh")
    ResponseEntity<LoginResponse> refreshToken(@CookieValue("refreshToken") String refreshToken);
}