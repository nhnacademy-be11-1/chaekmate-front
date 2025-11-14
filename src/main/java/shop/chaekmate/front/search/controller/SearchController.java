package shop.chaekmate.front.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.search.adaptor.SearchAdaptor;
import shop.chaekmate.front.search.dto.SearchResultResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchAdaptor adaptor;

    @GetMapping("/search")
    public String search(
            @RequestParam String prompt,
            @PageableDefault(sort = "publicationDatetime", direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model) {
        CommonResponse<Page<SearchResultResponse>> response = adaptor.getSearch(prompt, pageable);
        Page<SearchResultResponse> page = response.data();

        model.addAttribute("books", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("prompt", prompt);
        return "search/search-list";
    }
}
