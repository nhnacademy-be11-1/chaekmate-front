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
        model.addAttribute("slide1", slide1);
        model.addAttribute("slide2", slide2);
        model.addAttribute("slide3", slide3);

        dummy book : id, title, author, price, salsePrice, rating, reviewCount, imageUrl
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
