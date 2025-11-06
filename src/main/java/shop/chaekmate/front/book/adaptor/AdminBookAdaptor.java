package shop.chaekmate.front.book.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import shop.chaekmate.front.book.dto.response.AdminBookResponse;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "admin-book-adaptor", url = "${chaekmate.gateway.url}")
public interface AdminBookAdaptor {

    @GetMapping("/admin/books/recent")
    CommonResponse<List<AdminBookResponse>> getBooks(@RequestParam int limit);

}
