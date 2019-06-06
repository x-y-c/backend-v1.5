package yangchen.exam.service.examInfo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.repo.examInfoRepo;
import yangchen.exam.service.examInfo.ExamInfoService;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Autowired
    private yangchen.exam.repo.examinationRepo examinationRepo;


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
    public List<ExamInfo> getEndedExamInfo(Long studentId, Timestamp timestamp) {
        List<ExamInfo> endExam = examInfoRepo.findByStudentNumberAndExamEndBefore(studentId, timestamp);
        return endExam;
    }

    @Override
    public List<ExamInfo> getUnstartExamInfo(Long studentId, Timestamp timestamp) {
        List<ExamInfo> unstartedExam = examInfoRepo.findByStudentNumberAndExamStartAfter(studentId, timestamp);
        return unstartedExam;
    }

    @Override
    public List<ExamInfo> getIngExamInfo(Long studentId, Timestamp timestamp) {
        List<ExamInfo> doingExam = examInfoRepo.findByStudentNumberAndExamStartAfter(studentId, timestamp);
        List<ExamInfo> result = new ArrayList<>(doingExam.size());
        doingExam.forEach(examInfo -> {
            //符合时间，且没有交卷的
            if (examinationRepo.findById(examInfo.getExaminationId()).get().getUsed().equals(false)) {
                result.add(examInfo);
            }
        });
        return result;
    }

    @Override
    public List<ExamInfo> getFinishedExamInfo(Long studentId) {
        List<ExamInfo> finishedExam = examInfoRepo.getFinishedExam(studentId);
        return finishedExam;
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
