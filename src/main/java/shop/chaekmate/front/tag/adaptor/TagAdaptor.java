package shop.chaekmate.front.tag.adaptor;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.tag.dto.request.TagCreateRequest;
import shop.chaekmate.front.tag.dto.response.TagCreateResponse;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.tag.dto.response.TagPageResponse;
import shop.chaekmate.front.tag.dto.response.TagResponse;

@FeignClient(name="tag-client" , url = "${chaekmate.gateway.url}")
public interface TagAdaptor {

    @GetMapping(value = "/tags/paged")
    CommonResponse<TagPageResponse<TagResponse>> getPagedTags(@RequestParam("page") int page, @RequestParam("size") int size);
    
    @DeleteMapping(value ="/admin/tags/{id}")
    CommonResponse<Void> deleteTag(@PathVariable("id") Long id);

    @PostMapping(value = "/admin/tags")
    CommonResponse<TagCreateResponse> createTag(@RequestBody TagCreateRequest request);

    @GetMapping(value = "/tags")
    CommonResponse<List<TagResponse>> getAllTags();

}
