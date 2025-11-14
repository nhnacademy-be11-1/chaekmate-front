package shop.chaekmate.front.point.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.common.CommonResponse;
import shop.chaekmate.front.point.adaptor.PointPolicyAdaptor;
import shop.chaekmate.front.point.dto.request.PointPolicyCreateRequest;
import shop.chaekmate.front.point.dto.request.PointPolicyUpdateRequest;
import shop.chaekmate.front.point.dto.response.PointPolicyResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointPolicyService {
    private final PointPolicyAdaptor pointPolicyAdaptor;

    public List<PointPolicyResponse> getAllPointPolicy() {
        try {
            CommonResponse<List<PointPolicyResponse>> wrappedResponse = pointPolicyAdaptor.getAllPointPolicy();
            if (wrappedResponse == null || wrappedResponse.data() == null) {
                log.info("포인트 정책 데이터가 없습니다.");
                return List.of();
            }
            return wrappedResponse.data();
        } catch (FeignException.NotFound e) {
            log.error("백엔드 API 엔드포인트를 찾을 수 없습니다 (404). API 경로를 확인해주세요: {}", e.getMessage());
            return List.of();
        } catch (FeignException e) {
            log.error("백엔드 API 호출 실패 (상태코드: {}): {}", e.status(), e.getMessage(), e);
            return List.of();
        }
    }

    public void createPointPolicy(String earnedType, int point) {
        try {
            if (earnedType == null || earnedType.isEmpty()) {
                throw new IllegalArgumentException("포인트 정책 타입은 필수입니다.");
            }
            PointPolicyCreateRequest request = new PointPolicyCreateRequest(earnedType, point);
            pointPolicyAdaptor.createPointPolicy(request);
        } catch (FeignException.Conflict e) {
            log.error("중복된 포인트 정책: {}", earnedType);
            throw new RuntimeException("이미 존재하는 포인트 정책입니다.");
        } catch (FeignException.BadRequest e) {
            log.error("잘못된 요청: {}", e.getMessage());
            throw new RuntimeException("잘못된 요청입니다. 입력값을 확인해주세요.");
        } catch (FeignException e) {
            log.error("포인트 정책 생성 실패 (상태코드: {}): {}", e.status(), e.getMessage(), e);
            throw new RuntimeException("포인트 정책 생성에 실패했습니다.");
        }
    }

    public void deletePointPolicy(String type) {
        try {
            pointPolicyAdaptor.deletePointPolicy(type);
        } catch (FeignException e) {
            log.error("포인트 정책 삭제 실패: {}", e.getMessage(), e);
            throw new RuntimeException("포인트 정책 삭제에 실패했습니다.", e);
        }
    }

    public void updatePointPolicy(String type, int point) {
        try {
            PointPolicyUpdateRequest request = new PointPolicyUpdateRequest(type, point);
            pointPolicyAdaptor.updatePointPolicy(type, request);
        } catch (FeignException e) {
            log.error("포인트 정책 수정 실패 (상태코드: {}): {}", e.status(), e.getMessage(), e);
            throw new RuntimeException("포인트 정책 수정에 실패했습니다.", e);
        }
    }
}
