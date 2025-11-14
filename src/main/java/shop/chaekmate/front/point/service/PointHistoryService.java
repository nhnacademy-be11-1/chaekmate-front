package shop.chaekmate.front.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.point.adaptor.PointHistoryAdaptor;
import shop.chaekmate.front.point.dto.response.PointHistoryResponse;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
    private final PointHistoryAdaptor pointHistoryAdaptor;

    public Page<PointHistoryResponse> getAllPointHistory(Pageable pageable) {
        CommonResponse<Page<PointHistoryResponse>> response =
                pointHistoryAdaptor.getAllPointHistory(pageable);
        return response.data();
    }
}
