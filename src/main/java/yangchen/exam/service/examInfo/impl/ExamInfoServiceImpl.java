package yangchen.exam.service.examInfo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.model.ExamInfoResult;
import yangchen.exam.repo.ExamGroupRepo;
import yangchen.exam.repo.ExamInfoRepo;
import yangchen.exam.repo.ExamPaperRepo;
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
    private ExamInfoRepo examInfoRepo;

    @Autowired
    private ExamPaperRepo examPaperRepo;

    @Autowired
    private ExamGroupRepo examGroupRepo;

    @Override
    public ExamInfo addExamInfo(ExamInfo examInfo) {
        return examInfoRepo.save(examInfo);
    }

    @Override
    public List<ExamInfo> getExamInfoByStudentId(Integer studentId) {
        List<ExamInfo> byStudentNumber = examInfoRepo.getExamGroup(studentId);
        return byStudentNumber;
    }

    @Override
    public List<ExamInfo> getEndedExamInfo(Integer studentId, Timestamp timestamp) {
        List<ExamInfo> examInfoList = examInfoRepo.findByStudentNumber(studentId);
        List<ExamInfo> endExam = new ArrayList<>(examInfoList.size());
        examInfoList.parallelStream().forEach(examInfo -> {
            ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();
            if (examGroupNew.getEndTime().before(timestamp)) {
                endExam.add(examInfo);
            }
        });
        return endExam;
    }

    @Override
    public List<ExamInfo> getUnstartExamInfo(Integer studentId, Timestamp timestamp) {
        List<ExamInfo> examInfoList = examInfoRepo.findByStudentNumber(studentId);
        List<ExamInfo> unstartedExam = new ArrayList<>(examInfoList.size());
        examInfoList.parallelStream().forEach(examInfo -> {
            ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();
            if (examGroupNew.getBeginTime().after(timestamp)) {
                unstartedExam.add(examInfo);
            }
        });

        return unstartedExam;
    }

    @Override
    public List<ExamInfo> getIngExamInfo(Integer studentId, Timestamp timestamp) {
        List<ExamInfo> examInfoList = examInfoRepo.findByStudentNumber(studentId);
        List<ExamInfo> doingExam = new ArrayList<>(examInfoList.size());
        examInfoList.parallelStream().forEach(examInfo -> {
            ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();
            if (examGroupNew.getBeginTime().before(timestamp) && examGroupNew.getEndTime().after(timestamp)) {
                doingExam.add(examInfo);
            }
        });
//        List<ExamInfo> doingExam = examInfoRepo.findByStudentNumberAndExamStartBeforeAndExamEndAfter(studentId, timestamp, new Timestamp(System.currentTimeMillis()));
        List<ExamInfo> result = new ArrayList<>(doingExam.size());
        doingExam.forEach(examInfo -> {
            //符合时间，且没有交卷的
            if (examPaperRepo.findById(examInfo.getExaminationId()).get().getFinished().equals(false)) {
                result.add(examInfo);
            }
        });
        return result;
    }

    @Cacheable(value = "examInfo")
    @Override
    public ExamInfoResult getExamInfoResultByExaminationId(Integer examinationId) {
        ExamInfo examInfo = examInfoRepo.findByExaminationId(examinationId);
        ExamInfoResult examInfoResult = new ExamInfoResult(examInfo);
        ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();
        examInfoResult.setExamStart(examGroupNew.getBeginTime());
        examInfoResult.setExamEnd(examGroupNew.getEndTime());
        return examInfoResult;
    }

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
        ExamGroupNew examGroupNew = examGroupRepo.findById(byExaminationId.getExamGroupId()).get();
        return Math.toIntExact(examGroupNew.getExamTime());
    }
}
