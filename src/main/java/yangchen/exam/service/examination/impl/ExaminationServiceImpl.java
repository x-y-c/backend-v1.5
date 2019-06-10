package yangchen.exam.service.examination.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.*;
import yangchen.exam.model.*;
import yangchen.exam.repo.examinationRepo;
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
    private examinationRepo examinationRepo;

    @Autowired
    private ExamInfoService examInfoService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private yangchen.exam.repo.examInfoRepo examInfoRepo;

    @Autowired
    private yangchen.exam.repo.questionRepo questionRepo;


    @Override
    public Examination createExamInfo(String category) {

        List<Question> questionByCategory = questionService.findQuestionByCategory(category);
        StringBuilder titles = new StringBuilder();

        //这里其实只创建了一个人的考题，5道，用随机数工具保证不重复；
        Set random = RandomUtil.getRandom(0, questionByCategory.size() - 1, 5);
        for (Object s : random) {
            Question question = questionByCategory.get(Integer.valueOf(String.valueOf(s)));
            LOGGER.info(question.getQuestionName() + question.getCategory());
            titles.append(question.getId());
            titles.append(",");
        }

        Examination examination = new Examination();
        examination.setTitleType("小测验");
        examination.setActive(Boolean.TRUE);
        examination.setUsed(Boolean.FALSE);
        examination.setTitleId(titles.toString());

        examinationRepo.save(examination);

        return examination;


    }

    @Override
    public List<ExaminationDetail> examInfoDetail(Long studentId) {
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
    public List<ExaminationDetail> getEndedExamination(Long studentId) {
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        List<ExamInfo> endedExamInfo = examInfoService.getEndedExamInfo(studentId, timestamp1);
        List<ExaminationDetail> examinationDetails = changeExamInfo(endedExamInfo);
        return examinationDetails;
    }

    @Override
    public List<ExaminationDetail> getFinishedExamination(Long studentId) {
        List<ExamInfo> finishedExamInfo = examInfoService.getFinishedExamInfo(studentId);
        List<ExaminationDetail> examinationDetails = changeExamInfo(finishedExamInfo);
        return examinationDetails;
    }

    @Override
    public List<ExaminationDetail> getUnstartedExamination(Long studentId) {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        List<ExamInfo> unstartExamInfo = examInfoService.getUnstartExamInfo(studentId, current);
        List<ExaminationDetail> examinationDetails = changeExamInfo(unstartExamInfo);
        return examinationDetails;
    }

    @Override
    public List<ExaminationDetail> getIngExamination(Long studentId) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<ExamInfo> IngExamination = examInfoService.getIngExamInfo(studentId, timestamp);
        List<ExaminationDetail> examinationDetails = changeExamInfo(IngExamination);
        return examinationDetails;
    }


    @Override
    public Examination createExamInfo(String category, Integer number) {
        List<Question> questionList = questionService.findQuestionByCategory(category);
        Set random = RandomUtil.getRandom(0, questionList.size() - 1, number);
        StringBuilder titles = new StringBuilder();
        for (Object s : random) {
            Question question = questionList.get(Integer.valueOf(String.valueOf(s)));
            LOGGER.info(question.getQuestionName() + "\n" + question.getCategory());
            titles.append(question.getId());
            titles.append(",");
        }
        Examination examination = new Examination();
        examination.setTitleType("小测验");
        examination.setActive(Boolean.TRUE);
        examination.setUsed(Boolean.FALSE);
        examination.setTitleId(titles.toString());

        examinationRepo.save(examination);

        return examination;

    }

    @Override
    public void createExam(String category, Integer number, List<String> grade, Timestamp startTime, Timestamp endTime,
                           Long ttl, String desc) {

        List<Student> studentList = new ArrayList<>();


        //通过循环获取需要添加的全部学生；
        for (String studentGrade : grade) {
            studentList.addAll(studentService.getStudentListByGrade(studentGrade));
        }

        //这里调用 createExamInfo方法保存试卷题目到数据库


        //这里保存考试组的信息，即本次考试的班级，开始结束时间等信息；
        ExamGroup examGroup = new ExamGroup();
        examGroup.setBeginTime(startTime);
        examGroup.setEndTime(endTime);
        StringBuilder grades = new StringBuilder();
        for (String g : grade) {
            grades.append(g);
            grades.append(",");
        }
        examGroup.setClassName(grades.toString());
        examGroup.setExamTime(ttl);
        examGroup.setDesc(desc);
        examGroup.setExamType(ExamType.execise);
        ExamGroup examGroup1 = examGroupService.addExamGroup(examGroup);


        for (Student student : studentList) {
            Examination examination = createExamInfo(category, number);
            ExamInfo examInfo = new ExamInfo();
            examInfo.setStudentName(student.getName());
            examInfo.setStudentNumber(student.getStudentId());
            examInfo.setDesc(desc);
            examInfo.setCategory(category);
            examInfo.setExamStart(startTime);
            examInfo.setExamEnd(endTime);
            examInfo.setTtl(ttl);
            examInfo.setExamGroupId(examGroup1.getId());
            examInfo.setExaminationId(examination.getId());
            examInfoService.addExamInfo(examInfo);

        }

        //这里是试卷的分配情况，即该id的试卷分配给了谁
        //数据有冗余，为了查询的方便，
        //必须先保存组信息，然后，在保存以后，取得组的id；

    }

    @Override
    public void createExam(ExamCreatedParam examCreatedParam) {
        if (examCreatedParam.getNumber() == null) {
            examCreatedParam.setNumber(5);
        }
        /**
         *  createExam(String category, Integer number, List<String> grade, Timestamp startTime, Timestamp endTime,
         *                            Long ttl, String desc) {
         */
        createExam(examCreatedParam.getCategory(),
                examCreatedParam.getNumber(),
                examCreatedParam.getGrades(),
                examCreatedParam.getStart(),
                examCreatedParam.getEnd(),
                examCreatedParam.getTtl(),
                examCreatedParam.getDesc());
    }
//    ExamGroup examGroup = new ExamGroup();
//        examGroup.setBeginTime(startTime);
//        examGroup.setEndTime(endTime);
//    StringBuilder grades = new StringBuilder();
//        for (String g : grade) {
//        grades.append(g);
//        grades.append(",");
//    }
//        examGroup.setClassName(grades.toString());
//        examGroup.setExamTime(ttl);
//        examGroup.setDesc(desc);
//
//    ExamGroup examGroup1 = examGroupService.addExamGroup(examGroup);

    public ExamGroup createExam(ExamParam examParam) {
        ExamGroup examGroup = new ExamGroup();
        examGroup.setBeginTime(examParam.getBeginTime());
        examGroup.setEndTime(examParam.getEndTime());
        List<Student> studentList = new ArrayList<>();
        List<String> grades = examParam.getGrades();
        StringBuilder gradeStr = new StringBuilder();
        for (String g : grades) {
            gradeStr.append(g);
            gradeStr.append(",");
        }
        examGroup.setClassName(gradeStr.toString());
        examGroup.setExamTime(examParam.getTtl());
        examGroup.setDesc(examParam.getTitle());
        examGroup.setExamType(examParam.getExamType());

        grades.forEach(s -> {
            List<Student> classMate = studentService.getStudentListByGrade(s);
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

        ExamGroup examGroup1 = examGroupService.addExamGroup(examGroup);
        return examGroup1;
    }


    public Boolean examTask(Student student, List<List<Question>> questionList, ExamParam examParam) {
        Examination examination = new Examination();
        StringBuilder stringBuilder = new StringBuilder();
        for (List<Question> questions : questionList) {
            Set random = RandomUtil.getRandom(0, questions.size() - 1, 0);
            for (Object o : random) {
                //获取一道题目
                Question question = questions.get(Integer.valueOf(String.valueOf(o)));
                stringBuilder.append(question.getId());
                stringBuilder.append(",");
            }
        }
        examination.setUsed(Boolean.FALSE);
        examination.setActive(Boolean.TRUE);
        examination.setTitleId(stringBuilder.toString());
        Examination save = examinationRepo.save(examination);
        ExamInfo examInfo = new ExamInfo();
        examInfo.setStudentName(student.getName());//姓名
        examInfo.setStudentNumber(student.getStudentId());//学号
        examInfo.setTtl(examParam.getTtl());//时长
        examInfo.setExaminationId(save.getId());//试卷编号
        examInfo.setExamStart(examParam.getBeginTime());//开始时间
        examInfo.setExamEnd(examParam.getEndTime());//截止时间
        examInfo.setDesc(examParam.getTitle());//题目
        if (examParam.getExamType().equals(ExamType.execise)) {
            examInfo.setCategory("练习");
        } else {
            examInfo.setCategory("考试");
        }
        ExamInfo examInfo1 = examInfoService.addExamInfo(examInfo);
        return examInfo1 != null;

    }

    @Override
    public List<Examination> getUnUsedExamination() {
        return examinationRepo.findByUsed(Boolean.FALSE);
    }

    @Override
    public List<Examination> getUsedExamination() {
        return examinationRepo.findByUsed(Boolean.TRUE);
    }

    @Override
    public List<QuestionDetail> getQuestionInfo(Integer id) {
        Optional<Examination> byId = examinationRepo.findById(id);
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
        Optional<Examination> byId = examinationRepo.findById(id);
        Examination examination = byId.get();
        if (examination.getUsed() == Boolean.TRUE) {
            return QuestionResult.builder().used(1).questionInfo(null).build();
        } else {
            return QuestionResult.builder().used(0).questionInfo(getQuestionInfo(id)).build();
        }
    }


    @Override
    public Examination getExaminationById(Integer id) {
        return examinationRepo.findById(id).get();
    }

    @Override
    public Boolean submitTest(Integer id, Long studentId) {
        Examination examination = examinationRepo.findById(id).get();
        examination.setUsed(Boolean.TRUE);
        Examination save = examinationRepo.save(examination);
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
