package shop.chaekmate.front.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.LikeAdaptor;
import shop.chaekmate.front.book.dto.response.LikeResponse;
import shop.chaekmate.front.common.CommonResponse;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeAdaptor likeAdaptor;

    public boolean toggleLike(Long bookId, String actionType){
        if(actionType.equals("like")){
            likeAdaptor.createLike(bookId);
            return true;
        }
        if(actionType.equals("unlike")){
            likeAdaptor.deleteLike(bookId);
            return false;
        }

        return false;
    }

    public List<Long> getMemberLikedBook(){
        CommonResponse<List<LikeResponse>> wrappedLikes = null;

        try {
            wrappedLikes = likeAdaptor.getMemberLikes();
        } catch (Exception e){
            return List.of();
        }

        return wrappedLikes.data().stream().map(LikeResponse::bookId).toList();

    }

}
