package shop.chaekmate.front.book.adaptor;

import java.util.concurrent.CompletableFuture;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.chaekmate.front.common.CommonResponse;

@FeignClient(name = "bookViewCount-client", url = "${chaekmate.gateway.url}")
public interface BookViewCountAdaptor {

    @Async
    @PostMapping(path="/books/{bookId}/views")
    CompletableFuture<CommonResponse<Void>> increaseView(@PathVariable Long bookId);

}
