package yangchen.exam.service.examination.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.*;
import yangchen.exam.model.*;
import yangchen.exam.repo.ExamPaperRepo;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.examination.ExamGroupService;
import yangchen.exam.service.examination.ExaminationService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.score.ScoreService;
import yangchen.exam.service.student.studentService;
import yangchen.exam.util.RandomUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author YC
 * @date 2019/5/5 15:35
 * O(∩_∩)O)
 */
@Service
public class ExaminationServiceImpl implements ExaminationService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExaminationServiceImpl.class);
    @Autowired
    private QuestionService questionService;

    @Autowired
    private studentService studentService;

    @Autowired
    private ExamGroupService examGroupService;

    @Autowired
    private ExamPaperRepo examPaperRepo;

    @Autowired
    private ExamInfoService examInfoService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private yangchen.exam.repo.examInfoRepo examInfoRepo;

    @Autowired
    private yangchen.exam.repo.questionRepo questionRepo;


    @Override
    public List<ExaminationDetail> examInfoDetail(Integer studentId) {
        List<ExamInfo> examInfoByStudentId = examInfoService.getExamInfoByStudentId(studentId);
        List<ExaminationDetail> examinationDetails = changeExamInfo(examInfoByStudentId);
        return examinationDetails;
    }


    public static List<ExaminationDetail> changeExamInfo(List<ExamInfo> list) {
        List<ExaminationDetail> examinationDetails = new ArrayList<>(list.size());
        for (ExamInfo examInfo : list) {
            ExaminationDetail examinationDetail = new ExaminationDetail();
            examinationDetail.setCategory(examInfo.getCategory());
            examinationDetail.setId(examInfo.getExaminationId());
            examinationDetail.setDesc(examInfo.getDesc());
            examinationDetail.setEnd(examInfo.getExamEnd());
            examinationDetail.setStart(examInfo.getExamStart());
            examinationDetail.setTtl(examInfo.getTtl());
            examinationDetails.add(examinationDetail);
        }
        return examinationDetails;

    }

    @Override
    public List<ExaminationDetail> getEndedExamination(Integer studentId) {
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        List<ExamInfo> endedExamInfo = examInfoService.getEndedExamInfo(studentId, timestamp1);
        List<ExaminationDetail> examinationDetails = changeExamInfo(endedExamInfo);
        return examinationDetails;
    }

    @Override
    public List<ExaminationDetail> getFinishedExamination(Integer studentId) {
        List<ExamInfo> finishedExamInfo = examInfoService.getFinishedExamInfo(studentId);
        List<ExaminationDetail> examinationDetails = changeExamInfo(finishedExamInfo);
        return examinationDetails;
    }

    @Override
    public List<ExaminationDetail> getUnstartedExamination(Integer studentId) {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        List<ExamInfo> unstartExamInfo = examInfoService.getUnstartExamInfo(studentId, current);
        List<ExaminationDetail> examinationDetails = changeExamInfo(unstartExamInfo);
        return examinationDetails;
    }

    @Override
    public List<ExaminationDetail> getIngExamination(Integer studentId) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<ExamInfo> IngExamination = examInfoService.getIngExamInfo(studentId, timestamp);
        List<ExaminationDetail> examinationDetails = changeExamInfo(IngExamination);
        return examinationDetails;
    }


    public ExamGroupNew createExam(ExamParam examParam) {
        ExamGroupNew examGroup = new ExamGroupNew();
        examGroup.setBeginTime(examParam.getBeginTime());
        //这里，考试只需要考试开始时间，在开始时间的基础上加上考试时长，就是结束时间；
        Timestamp beginTime = examParam.getBeginTime();
        long time = beginTime.getTime();
        //examParam.getTtl() 单位是秒，时间戳的单位是毫秒，所以，取出ttl*1000，转换为ms；
        long endTime = time + examParam.getTtl() * 1000;
        examGroup.setEndTime(new Timestamp(endTime));
        List<StudentNew> studentList = new ArrayList<>();
        List<String> grades = examParam.getGrades();
        StringBuilder gradeStr = new StringBuilder();
        for (String g : grades) {
            gradeStr.append(g);
            gradeStr.append(",");
        }
        examGroup.setClassName(gradeStr.toString());
        examGroup.setExamTime(examParam.getTtl());
        examGroup.setExamDesc(examParam.getExamName());

        grades.forEach(s -> {
            List<StudentNew> classMate = studentService.getStudentListByGrade(s);
            studentList.addAll(classMate);
        });
        List<TwoTuple<String, String>> examList = examParam.getExam();
        List<List<Question>> questionList = new ArrayList<>();
        for (TwoTuple<String, String> exam : examList) {
            List<Question> result = questionRepo.findByCategoryAndDifficulty(exam.first, exam.second);
            questionList.add(result);
        }

        studentList.forEach(student -> {
            Boolean aBoolean = examTask(student, questionList, examParam);
        });

        ExamGroupNew examGroup1 = examGroupService.addExamGroup(examGroup);
        return examGroup1;
    }


    public Boolean examTask(StudentNew student, List<List<Question>> questionList, ExamParam examParam) {
        ExamPaper examPaper = new ExamPaper();
        StringBuilder stringBuilder = new StringBuilder();
        for (List<Question> questions : questionList) {
            Set random = RandomUtil.getRandom(0, questions.size() - 1, 1);
            for (Object o : random) {
                //获取一道题目
                Question question = questions.get(Integer.valueOf(String.valueOf(o)));
                stringBuilder.append(question.getId());
                stringBuilder.append(",");
            }
        }
        examPaper.setFinished(Boolean.FALSE);
        examPaper.setTitleId(stringBuilder.toString());
        ExamPaper savedExamPaper = examPaperRepo.save(examPaper);
        ExamInfo examInfo = new ExamInfo();
        examInfo.setStudentName(student.getStudentName());//姓名
        examInfo.setStudentNumber(student.getStudentId());//学号
        examInfo.setTtl(Long.valueOf(examParam.getTtl()));//时长
        examInfo.setExaminationId(savedExamPaper.getId());//试卷编号
        examInfo.setExamStart(examParam.getBeginTime());//开始时间


        Timestamp beginTime = examParam.getBeginTime();
        long time = beginTime.getTime();
        //examParam.getTtl() 单位是秒，时间戳的单位是毫秒，所以，取出ttl*1000，转换为ms；
        long endTime = time + examParam.getTtl() * 1000;
        examInfo.setExamEnd(new Timestamp(endTime));//截止时间
        examInfo.setDesc(examParam.getExamName());//题目
        ExamInfo examInfo1 = examInfoService.addExamInfo(examInfo);
        return examInfo1 != null;

    }

    @Override
    public List<ExamPaper> getUnUsedExamination() {
        return examPaperRepo.findByFinished(Boolean.FALSE);
    }

    @Override
    public List<ExamPaper> getUsedExamination() {
        return examPaperRepo.findByFinished(Boolean.TRUE);
    }

    @Override
    public List<QuestionDetail> getQuestionInfo(Integer id) {
        Optional<ExamPaper> byId = examPaperRepo.findById(id);
        List<QuestionDetail> result = new ArrayList<>();
        String titles = byId.get().getTitleId();
        String[] split = titles.split(",");
        LOGGER.info(String.valueOf(split.length));
//        LOGGER.info(split[0] + "\n" + split[1] + "\n" + split[2] + "\n");
        for (String title : split) {
            Question questionById = questionService.findQuestionById(Integer.valueOf(title));
            if (questionById != null) {
                QuestionDetail questionDetail = new QuestionDetail();
                questionDetail.setQuestion(questionById.getDescription());
                questionDetail.setTitle(questionById.getQuestionName());
                result.add(questionDetail);
            }
        }
        return result;
    }

    @Override
    public QuestionResult getQuestionInfoResult(Integer id) {
        Optional<ExamPaper> byId = examPaperRepo.findById(id);
        ExamPaper examination = byId.get();
        if (examination.getFinished() == Boolean.TRUE) {
            return QuestionResult.builder().used(1).questionInfo(null).build();
        } else {
            return QuestionResult.builder().used(0).questionInfo(getQuestionInfo(id)).build();
        }
    }


    @Override
    public ExamPaper getExaminationById(Integer id) {
        return examPaperRepo.findById(id).get();
    }

    @Override
    public Boolean submitTest(Integer id, Integer studentId) {
        ExamPaper examination = examPaperRepo.findById(id).get();
        examination.setFinished(Boolean.TRUE);
        ExamPaper save = examPaperRepo.save(examination);
        Integer finalScore = 0;
        List<Score> byExaminationAndStudentId = scoreService.findByExaminationAndStudentId(examination.getId(), studentId);
        for (Score score1 : byExaminationAndStudentId) {
            finalScore = finalScore + score1.getScore();
        }
        finalScore = finalScore / byExaminationAndStudentId.size();
        ExamInfo examInfoByExaminationId = examInfoService.getExamInfoByExaminationId(examination.getId());
        examInfoByExaminationId.setExaminationScore(finalScore);
        examInfoRepo.save(examInfoByExaminationId);
        return save != null;
    }

}
