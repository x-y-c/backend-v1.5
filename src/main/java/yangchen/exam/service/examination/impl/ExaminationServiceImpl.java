package yangchen.exam.service.examination.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.DifficultEnum;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.*;
import yangchen.exam.model.*;
import yangchen.exam.repo.ExamInfoRepo;
import yangchen.exam.repo.ExamPaperRepo;
import yangchen.exam.repo.QuestionRepo;
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
    private ExamInfoRepo examInfoRepo;

    @Autowired
    private QuestionRepo questionRepo;


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

//    @Override
//    public List<ExaminationDetail> getFinishedExamination(Integer studentId) {
//        List<ExamInfo> finishedExamInfo = examInfoService.getFinishedExamInfo(studentId);
//        List<ExaminationDetail> examinationDetails = changeExamInfo(finishedExamInfo);
//        return examinationDetails;
//    }

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
        long endTime = time + examParam.getTtl() * 1000 * 60;
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
        examList.forEach(examStageAndDiff -> {
            examStageAndDiff.setFirst(StageEnum.getStageCode(examStageAndDiff.getFirst()));
            examStageAndDiff.setSecond(DifficultEnum.getDifficultCode(examStageAndDiff.getSecond()));
        });
        List<List<QuestionNew>> questionList = new ArrayList<>();
        for (TwoTuple<String, String> exam : examList) {
            //List<QuestionNew> result = questionRepo.findByStageAndDifficulty(exam.first, exam.second);
            List<QuestionNew> result = questionRepo.findByStageAndDifficultyAndQuestionType(exam.first, exam.second, "1000206");
            questionList.add(result);
        }
        ExamGroupNew examGroup1 = examGroupService.addExamGroup(examGroup);

        studentList.forEach(student -> {
            Boolean aBoolean = examTask(student, questionList, examParam, examGroup1.getId());
        });

        return examGroup1;
    }

    @Override
    public List<ExamPageInfo> getExamPageInfo(Integer examGroupId) {
        List<ExamInfo> examInfoList = examInfoService.getExamInfoByExamGroup(examGroupId);
        List<ExamPageInfo> examInforesult = new ArrayList<>();
        for (ExamInfo examInfo : examInfoList) {
            ExamPageInfo examPageInfo = new ExamPageInfo();
            StudentNew student = studentService.getStudentByStudentId(examInfo.getStudentNumber());
            examPageInfo.setStudentGrade(student.getStudentGrade());
            examPageInfo.setStudentName(student.getStudentName());
            examPageInfo.setStudentId(student.getStudentId());
            List<QuestionInfo> questionNamesByExamPages = questionService.getQuestionNamesByExamPage(examInfo.getExaminationId());
            examPageInfo.setQuestionList(questionNamesByExamPages);
            examInforesult.add(examPageInfo);
        }
        return examInforesult;
    }

    @Override
    public ExamPaper getExampaperByExamPaper(Integer examPaperId) {
        ExamPaper examPaper = examPaperRepo.findById(examPaperId).get();
        return examPaper;
    }


    public Boolean examTask(StudentNew student, List<List<QuestionNew>> questionList, ExamParam examParam, Integer examPaperId) {
        ExamPaper examPaper = new ExamPaper();
        StringBuilder stringBuilder = new StringBuilder();
        for (List<QuestionNew> questions : questionList) {
            Set random = RandomUtil.getRandom(0, questions.size() - 1, 1);
            for (Object o : random) {
                //获取一道题目
                QuestionNew question = questions.get(Integer.valueOf(String.valueOf(o)));
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
        examInfo.setExamStart(examParam.getBeginTime());
        examInfo.setExamGroupId(examPaperId);
        examInfo.setExaminationScore(0);


        Timestamp beginTime = examParam.getBeginTime();
        long time = beginTime.getTime();
        //examParam.getTtl() 单位是秒，时间戳的单位是毫秒，所以，取出ttl*1000，转换为ms；
        long endTime = time + examParam.getTtl() * 1000 * 60;
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
            QuestionNew questionById = questionService.findQuestionById(Integer.valueOf(title));
            if (questionById != null) {
                QuestionDetail questionDetail = new QuestionDetail();
                questionDetail.setQuestion(questionById.getQuestionDetails());
                questionDetail.setTitle(questionById.getQuestionName());
                questionDetail.setCustomBh(questionById.getCustomBh());
                questionDetail.setId(String.valueOf(questionById.getId()));
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
        if (byExaminationAndStudentId.size() == 0) {
            finalScore = 0;
        } else {
            /**
             * 题目数就是5道题啊，总成成绩= 每道题成绩/总题数，
             * 但是题目数就是5道，所以，可以直接除以5
             */
            finalScore = finalScore / 5;
        }
        ExamInfo examInfoByExaminationId = examInfoService.getExamInfoByExaminationId(examination.getId());
        examInfoByExaminationId.setExaminationScore(finalScore);
        examInfoRepo.save(examInfoByExaminationId);
        return save != null;
    }

}
