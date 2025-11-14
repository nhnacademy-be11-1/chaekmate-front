package shop.chaekmate.front.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.chaekmate.front.member.adaptor.MemberAdaptor;

import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberProxyController {

    private final MemberAdaptor memberAdaptor;

    @GetMapping("/check-login-id")
    public Map<String, Object> checkLoginId(@RequestParam String loginId) {
        return memberAdaptor.checkLoginId(loginId).data();
    }

    @GetMapping("/check-email")
    public Map<String, Object> checkEmail(@RequestParam String email) {
        return memberAdaptor.checkEmail(email).data();
    }
}
