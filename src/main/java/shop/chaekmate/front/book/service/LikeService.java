package shop.chaekmate.front.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.book.adaptor.LikeAdaptor;

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
}
