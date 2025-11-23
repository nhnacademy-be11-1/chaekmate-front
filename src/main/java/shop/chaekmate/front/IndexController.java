package shop.chaekmate.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {

        /*
        slide : subtitle, title, description, imageUrl, bookId
        model.addAttribute("slide1", slide1); 체크메이트추천
        model.addAttribute("slide2", slide2); 이달의 추천도서
        model.addAttribute("slide3", slide3); 실시간 조회수 급상승
        model.addAttribute("banner1", nanner1); 우측 배너1
        model.addAttribute("banner2", banner2); 우측 배너 2


        dummy book : id, title, author, price, salesPrice, rating, reviewCount, imageUrl
        model.addAttribute("recentBooks", dummyBooks);
        model.addAttribute("recommendedBooks", dummyBooks);
        model.addAttribute("mostReviewedBooks", dummyBooks);
        model.addAttribute("earlyAdopterPicks", dummyBooks);
        */

        return "index";
    }

    @GetMapping("/admin")
    public String adminIndex(Model model){

        return "admin/admin-index";
    }

}
