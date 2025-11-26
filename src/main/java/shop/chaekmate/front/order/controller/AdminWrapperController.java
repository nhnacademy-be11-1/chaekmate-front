package shop.chaekmate.front.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.chaekmate.front.order.adaptor.WrapperAdaptor;
import shop.chaekmate.front.order.dto.request.WrapperRequest;

@Controller
@RequestMapping("/admin/wrappers")
@RequiredArgsConstructor
public class AdminWrapperController {

    private final WrapperAdaptor wrapperAdaptor;

    @GetMapping
    public String wrapperList(Model model) {
        var response = wrapperAdaptor.getWrappers(); // 전체 목록
        model.addAttribute("wrappers", response.data());

        return "admin/wrapper/admin-wrapper";
    }

    @PostMapping
    public String createWrapper(String name, Integer price) {
        WrapperRequest request = new WrapperRequest(name, price);
        wrapperAdaptor.createWrapper(request);
        return "redirect:/admin/wrappers";
    }

    @PostMapping("/{id}")
    public String modifyWrapper(@PathVariable Long id, String name, Integer price) {
        WrapperRequest request = new WrapperRequest(name, price);
        wrapperAdaptor.modifyWrapper(id, request);
        return "redirect:/admin/wrappers";
    }


    @DeleteMapping("/{id}")
    public String deleteWrapper(@PathVariable Long id) {
        wrapperAdaptor.deleteWrapper(id);
        return "redirect:/admin/wrappers";
    }
}
