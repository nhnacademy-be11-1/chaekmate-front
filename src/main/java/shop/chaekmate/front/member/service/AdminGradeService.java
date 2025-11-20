package shop.chaekmate.front.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.chaekmate.front.member.adaptor.AdminGradeAdaptor;
import shop.chaekmate.front.member.dto.request.CreateGradeRequest;
import shop.chaekmate.front.member.dto.request.UpdateGradeRequest;

@Service
@RequiredArgsConstructor
public class AdminGradeService {
    private final AdminGradeAdaptor adminGradeAdaptor;

    public void updateGrade(Long gradeId, UpdateGradeRequest request) {
        adminGradeAdaptor.updateGrade(gradeId, request);
    }
    public void createGrade(CreateGradeRequest request) {
        adminGradeAdaptor.createGrade(request);
    }
    public void deleteGrade(Long gradeId) {
        adminGradeAdaptor.deleteGrade(gradeId);
    }
}