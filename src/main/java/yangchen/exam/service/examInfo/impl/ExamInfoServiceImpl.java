package yangchen.exam.service.examInfo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.repo.examInfoRepo;
import yangchen.exam.service.examInfo.ExamInfoService;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/6 21:37
 * O(∩_∩)O)
 */
@Service
public class ExamInfoServiceImpl implements ExamInfoService {

    @Autowired
    private examInfoRepo examInfoRepo;


    @Override
    public ExamInfo addExamInfo(ExamInfo examInfo) {
        return examInfoRepo.save(examInfo);
    }

    @Override
    public List<ExamInfo> getExamInfoByStudentId(Long studentId) {
        List<ExamInfo> byStudentNumber = examInfoRepo.findByStudentNumber(studentId);
        return byStudentNumber;
    }

    @Override
    public ExamInfo getExamInfoByExaminationId(Integer examinationId) {
        return examInfoRepo.findByExaminationId(examinationId);
    }

    @Override
    public List<ExamInfo> getExamInfoByExamGroup(Integer examGroupId) {
        return examInfoRepo.findByExamGroupId(examGroupId);
    }
}
