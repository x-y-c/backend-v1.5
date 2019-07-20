package yangchen.exam.service.examInfo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.repo.ExamPaperRepo;
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
    private ExamPaperRepo examPaperRepo;

    @Override
    public ExamInfo addExamInfo(ExamInfo examInfo) {
        return examInfoRepo.save(examInfo);
    }

    @Override
    public List<ExamInfo> getExamInfoByStudentId(Integer studentId) {
        List<ExamInfo> byStudentNumber = examInfoRepo.findByStudentNumber(studentId);
        return byStudentNumber;
    }

    @Override
    public List<ExamInfo> getEndedExamInfo(Integer studentId, Timestamp timestamp) {
        List<ExamInfo> endExam = examInfoRepo.findByStudentNumberAndExamEndBefore(studentId, timestamp);
        return endExam;
    }

    @Override
    public List<ExamInfo> getUnstartExamInfo(Integer studentId, Timestamp timestamp) {
        List<ExamInfo> unstartedExam = examInfoRepo.findByStudentNumberAndExamStartAfter(studentId, timestamp);
        return unstartedExam;
    }

    @Override
    public List<ExamInfo> getIngExamInfo(Integer studentId, Timestamp timestamp) {
        List<ExamInfo> doingExam = examInfoRepo.findByStudentNumberAndExamStartBeforeAndExamEndAfter(studentId, timestamp, new Timestamp(System.currentTimeMillis()));
        List<ExamInfo> result = new ArrayList<>(doingExam.size());
        doingExam.forEach(examInfo -> {
            //符合时间，且没有交卷的
            if (examPaperRepo.findById(examInfo.getExaminationId()).get().getFinished().equals(false)) {
                result.add(examInfo);
            }
        });
        return result;
    }

    @Override
    public List<ExamInfo> getFinishedExamInfo(Integer studentId) {
        List<ExamInfo> finishedExam = examInfoRepo.getFinishedExam(studentId);
        return finishedExam;
    }


    @Cacheable(value = "examInfo")
    @Override
    public ExamInfo getExamInfoByExaminationId(Integer examinationId) {
        return examInfoRepo.findByExaminationId(examinationId);
    }
@Cacheable(value = "examInfoLists")
    @Override
    public List<ExamInfo> getExamInfoByExamGroupId(Integer examGroupId) {
        List<ExamInfo> examInfoList = examInfoRepo.findByExamGroupId(examGroupId);
        return examInfoList;
    }

    @Cacheable(value = "examInfoList")
    @Override
    public List<ExamInfo> getExamInfoByExamGroup(Integer examGroupId) {
        return examInfoRepo.findByExamGroupId(examGroupId);
    }

    @Override
    public Integer getTtl(Integer examinationId) {
        ExamInfo byExaminationId = examInfoRepo.findByExaminationId(examinationId);
        return Math.toIntExact(byExaminationId.getTtl());
    }
}
