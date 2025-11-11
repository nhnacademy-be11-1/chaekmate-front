package shop.chaekmate.front.search.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.search.dto.SearchResultResponse;

@FeignClient(name="search-client" , url = "${chaekmate.gateway.url}")
public interface SearchAdaptor {

    @GetMapping(value = "/search")
    CommonResponse<Page<SearchResultResponse>> getSearch(@RequestParam("prompt") String prompt , @PageableDefault(size = 10, sort = "publicationDatetime", direction = Sort.Direction.DESC)Pageable pageable);

    @GetMapping(value = "/search/recommendKeyword")
    CommonResponse<Void> getRecommendKeyword(@RequestParam("prompt") String prompt);
}
