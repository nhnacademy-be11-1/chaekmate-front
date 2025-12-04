package shop.chaekmate.front.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CustomErrorController {

    @GetMapping("/error/{statusCode}")
    public String errorPage(@PathVariable String statusCode, Model model) {
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("title", "오류 - Chaekmate");

        return "error/" + statusCode;
    }
}

