package shop.chaekmate.front.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin/index";
    }
}
