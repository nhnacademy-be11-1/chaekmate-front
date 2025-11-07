package shop.chaekmate.front.tag.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.tag.dto.request.TagCreateRequest;
import shop.chaekmate.front.tag.dto.response.TagPageResponse;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.tag.adaptor.TagAdaptor;
import shop.chaekmate.front.tag.dto.response.TagResponse;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagAdaptor tagAdaptor;

    public TagPageResponse<TagResponse> getPagedTags(int page, int size){
        CommonResponse<TagPageResponse<TagResponse>> wrappedResponse = tagAdaptor.getPagedTags(page, size);

        return wrappedResponse.data();
    }

    public void deleteTagById(Long id){

        tagAdaptor.deleteTag(id);

    }

    public void createTag(String name){

        TagCreateRequest request = new TagCreateRequest(name);
        tagAdaptor.createTag(request);

    }

    // 태그 아이디로 태그 이름들 찾아주는 메소드
    public List<String> findNamesByIds(List<Long> ids){
        CommonResponse<List<TagResponse>> wrappedResponse = tagAdaptor.getAllTags();
        List<TagResponse> tags = wrappedResponse.data();

        return tags.stream().filter(t->ids.contains(t.id())).map(TagResponse::name).toList();
    }
}
