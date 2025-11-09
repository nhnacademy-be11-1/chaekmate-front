package shop.chaekmate.front.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/order")
    public String orderPage(Model model) {
//        return "order/orderTest";
        return "order/orderPage";
    }
}
