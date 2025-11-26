package shop.chaekmate.front.order.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.order.dto.request.WrapperRequest;
import shop.chaekmate.front.order.dto.response.WrapperResponse;

@FeignClient(name = "wrapper-client", url = "${chaekmate.gateway.url}")
public interface WrapperAdaptor {

    // admin
    @PostMapping("/admin/wrappers")
    CommonResponse<WrapperResponse> createWrapper(@RequestBody WrapperRequest request);

    @PutMapping("/admin/wrappers/{id}")
    CommonResponse<WrapperResponse> modifyWrapper(@PathVariable("id") Long id,
                                                  @RequestBody WrapperRequest request);

    @DeleteMapping("/admin/wrappers/{id}")
    CommonResponse<Void> deleteWrapper(@PathVariable("id") Long id);

    // user
    @GetMapping("/wrappers")
    CommonResponse<List<WrapperResponse>> getWrappers();

}
