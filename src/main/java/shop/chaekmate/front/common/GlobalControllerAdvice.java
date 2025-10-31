package shop.chaekmate.front.common;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.chaekmate.front.client.CoreClient;
import shop.chaekmate.front.admin.category.CategoryResponse;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CoreClient coreClient;

    // allCategories 는전부
    @ModelAttribute("allCategories")
    public List<CategoryResponse> populateCategories() {
        return coreClient.getAllCategories();
    }

}
