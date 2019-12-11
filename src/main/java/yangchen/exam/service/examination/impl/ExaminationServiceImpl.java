package yangchen.exam.service.examination.impl;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.DifficultEnum;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.*;
import yangchen.exam.model.*;
import yangchen.exam.repo.*;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.examination.ExamGroupService;
import yangchen.exam.service.examination.ExaminationService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.score.ScoreService;
import yangchen.exam.service.student.StudentService;
import yangchen.exam.service.submit.SubmitService;
import yangchen.exam.util.DecodeQuestionDetails;
import yangchen.exam.util.RandomUtil;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author YC
 * @date 2019/5/5 15:35
 * O(∩_∩)O)
 */
@Service
public class ExaminationServiceImpl implements ExaminationService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExaminationServiceImpl.class);

    @Value("${image.nginx.url.path}")
    /*  http://211.68.35.79:2048  */
    private String imgNginxUrl;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private StudentService studentService;

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

    @Autowired
    private ExamGroupRepo examGroupRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private SubmitService submitService;


    @Override
    public List<ExaminationDetail> examInfoDetail(Integer studentId) {
        List<ExamInfo> examInfoByStudentId = examInfoService.getExamInfoByStudentId(studentId);
        List<ExaminationDetail> examinationDetails = changeExamInfo(examInfoByStudentId);
        return examinationDetails;
    }


    public List<ExaminationDetail> changeExamInfo(List<ExamInfo> list) {
        List<ExaminationDetail> examinationDetails = new ArrayList<>(list.size());
        for (ExamInfo examInfo : list) {
            //这个多线程会让前端显示的数据不按时间排列
//        list.parallelStream().forEach(examInfo -> {
          ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();
          ExaminationDetail examinationDetail = new ExaminationDetail();
          examinationDetail.setId(examInfo.getExaminationId());
          examinationDetail.setDesc(examGroupNew.getExamDesc());
          examinationDetail.setEnd(examGroupNew.getEndTime());
          examinationDetail.setStart(examGroupNew.getBeginTime());
          examinationDetail.setTtl(Long.valueOf(examGroupNew.getExamTime()));
          examinationDetails.add(examinationDetail);
//        });
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


    public JsonResult createExam(ExamParam examParam) {
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
        //todo 等确定下来记得修改哦
        Teacher byTeacherName = teacherRepo.findByTeacherName(examParam.getTeacherId());
        examGroup.setExamTeacher(String.valueOf(byTeacherName.getId()));
        examGroup.setClassName(gradeStr.toString());
        examGroup.setExamTime(examParam.getTtl());
        examGroup.setExamDesc(examParam.getExamName());

        grades.forEach(s -> {
            List<StudentNew> classMate = studentRepo.findByStudentGradeAndTeacherId(s,byTeacherName.getId());
            studentList.addAll(classMate);
        });


        List<TwoTuple<String, String>> examList = examParam.getExam();
        List<String> examListToString = new ArrayList<>();
        examList.forEach(examStageAndDiff -> {
            examStageAndDiff.setFirst(StageEnum.getStageCode(examStageAndDiff.getFirst()));
            examStageAndDiff.setSecond(DifficultEnum.getDifficultCode(examStageAndDiff.getSecond()));
            examListToString.add(examStageAndDiff.getFirst()+","+examStageAndDiff.getSecond());
        });
        List<List<QuestionNew>> questionList = new ArrayList<>();
        List<Integer> sizeList = new ArrayList<>();
        for (TwoTuple<String, String> exam : examList) {
            List<QuestionNew> result = questionRepo.findByStageAndDifficultyAndQuestionTypeAndActivedIsTrue(exam.first, exam.second, "1000206");
            sizeList.add(result.size());
            questionList.add(result);
        }
        //判断符不符合出题的资格
//        HashSet<String> hs =new HashSet<String>(examListToString);
//        for(String s:hs){
//            String spilted[]=s.split(",");
//            List<QuestionNew> result = questionRepo.findByStageAndDifficultyAndQuestionTypeAndActivedIsTrue(spilted[0], spilted[1], "1000206");
//        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        for(String item: examListToString){
            if(map.containsKey(item)){
                map.put(item, map.get(item).intValue() + 1);
            }else{
                map.put(item, new Integer(1));
            }
        }
        for(String key:map.keySet()){
            String spilted[]=key.split(",");
            List<QuestionNew> result = questionRepo.findByStageAndDifficultyAndQuestionTypeAndActivedIsTrue(spilted[0], spilted[1], "1000206");
            if(map.get(key)>result.size()){
                LOGGER.error("创建考试失败:[{}][{}]只有[{}]道题，不满足出卷要求",StageEnum.getStageName(spilted[0]),DifficultEnum.getDifficultName(spilted[1]),result.size());
                return JsonResult.errorResult(ResultCode.QUESTION_NUM_ERROR,
                        "创建考试失败:["+StageEnum.getStageName(spilted[0])+"]["+DifficultEnum.getDifficultName(spilted[1])+"]只有["+result.size()+"]道题，不满足出卷要求",
                        "");
            }
        }
        ExamGroupNew examGroup1 = examGroupService.addExamGroup(examGroup);
        studentList.forEach(student -> {
            Boolean aBoolean = examTask(student, questionList, examParam, examGroup1.getId());
        });



//        return examGroup1;
        return JsonResult.succResult(examGroup1.toString());
    }

    @Override
    public List<ExamPageInfo> getExamPageInfo(Integer examGroupId) {
        List<ExamInfo> examInfoList = examInfoService.getExamInfoByExamGroup(examGroupId);
        List<ExamPageInfo> examInfoResult = new ArrayList<>();
        for (ExamInfo examInfo : examInfoList) {
            ExamPageInfo examPageInfo = new ExamPageInfo();
            StudentNew student = studentService.getStudentByStudentId(examInfo.getStudentNumber());
            examPageInfo.setStudentGrade(student.getStudentGrade());
            examPageInfo.setStudentName(student.getStudentName());
            examPageInfo.setStudentId(student.getStudentId());
            List<QuestionInfo> questionNamesByExamPages = questionService.getQuestionNamesByExamPage(examInfo.getExaminationId());
            examPageInfo.setQuestionList(questionNamesByExamPages);
            examInfoResult.add(examPageInfo);
        }
        return examInfoResult;
    }


    public List<ExamPageInfo> getExamPageInfoFast(Integer examGroupId) {
        List<ExamInfo> examInfoList = examInfoService.getExamInfoByExamGroup(examGroupId);
        List<ExamPageInfo> examInfoResult = new ArrayList<>();
        examInfoList.parallelStream().forEach(examInfo -> {
            ExamPageInfo examPageInfo = new ExamPageInfo();
            StudentNew student = studentService.getStudentByStudentId(examInfo.getStudentNumber());
            examPageInfo.setStudentGrade(student.getStudentGrade());
            examPageInfo.setStudentName(student.getStudentName());
            examPageInfo.setStudentId(student.getStudentId());
            List<QuestionInfo> questionNamesByExamPages = questionService.getQuestionNamesByExamPage(examInfo.getExaminationId());
            examPageInfo.setQuestionList(questionNamesByExamPages);
            examInfoResult.add(examPageInfo);
        });

        return examInfoResult;
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

                while(stringBuilder.toString().contains(question.getId().toString())){
//                    假设你要产生5到10之间的随机数，可以用下面方法：
//                    int Min = 5;    int Max = 10;
//                    int result = Min + (int)(Math.random() * ((Max - Min) + 1));
                    int randomNum = 0 + (int)(Math.random() * ((questions.size() - 1 - 0) + 1));
                    question = questions.get(randomNum);
                }
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
        examInfo.setExaminationId(savedExamPaper.getId());//试卷编号
        examInfo.setExamGroupId(examPaperId);
        examInfo.setExaminationScore(0);

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
        //id是exam_paper的id
        Optional<ExamPaper> byId = examPaperRepo.findById(id);
        List<QuestionDetail> result = new ArrayList<>();
        //titles是question那张表里的id
        String titles = byId.get().getTitleId();
        String[] split = titles.split(",");
//        LOGGER.info(String.valueOf(split.length));
//        for (String title : split) {
        for (int i=0; i<split.length; i++){
            //QuestionNew questionById = questionService.findQuestionById(Integer.valueOf(title));
            QuestionNew questionById = questionService.findQuestionById(Integer.valueOf(split[i]));
            if (questionById != null) {
                QuestionDetail questionDetail = new QuestionDetail();
                //questionDetail.setQuestion(questionById.getQuestionDetails());
                questionDetail.setQuestion(DecodeQuestionDetails.getRightImage(imgNginxUrl, questionById.getQuestionDetails()));
                questionDetail.setTitle(questionById.getQuestionName());
                questionDetail.setCustomBh(questionById.getCustomBh());
                questionDetail.setId(String.valueOf(questionById.getId()));
                if ("100001".equals(questionById.getIsProgramBlank())) {
                    Gson gson = new Gson();
                    SourceCode sourceCode = gson.fromJson(questionById.getSourceCode(), SourceCode.class);
                    questionDetail.setSrc(sourceCode.getKey().get(0).getCode());
                } else {
                    questionDetail.setSrc("");
                }

                //i 是 index
                //score 的来源是score表里的 定位examnationId和number去定位唯一score
                Score score = scoreService.findByExaminationIdAndIndex(id,i);
                if(score!=null){
                    questionDetail.setScore(score.getScore());
                }
                //codeHistory的来源是submit表里的 定位student_id和examination_id和questionId联合查询，按id降序找出top1
                Submit codeHistory = submitService.getCodeHistory(Integer.valueOf(split[i]),id);
                if(codeHistory!=null) {
                    questionDetail.setCodeHistory(codeHistory.getSrc());
                }
                result.add(questionDetail);
            }
        }
        return result;
    }

    @Override
    public JsonResult getQuestionInfoResult(Integer studentId, Integer id) {
        Optional<ExamPaper> byId = examPaperRepo.findById(id);
        ExamPaper examination = byId.get();
        ExamInfo examInfo = examInfoRepo.findByExaminationId(id);
        if (!examInfo.getStudentNumber().equals(studentId)) {
            return JsonResult.errorResult(ResultCode.NO_PERMISSION, "没有权限查看该试卷", null);
        }
        if (examination.getFinished() == Boolean.TRUE) {
            return JsonResult.succResult(QuestionResult.builder().used(1).questionInfo(null).build());
        }
        else {
            return JsonResult.succResult(QuestionResult.builder().used(0).questionInfo(getQuestionInfo(id)).build());
        }
    }


    @Override
    public ExamPaper getExaminationById(Integer id) {
        return examPaperRepo.findById(id).get();
    }

    @Override
    public Boolean submitTest(Integer id, Integer studentId,Integer sign) {
        ExamPaper examination = examPaperRepo.findById(id).get();
        examination.setFinished(Boolean.TRUE);

        if(sign==1) {
            LOGGER.info("学生[{}]交卷 备注:sign=[{}]，主动交卷", studentId, sign);
        }
        else if(sign==2){
            LOGGER.info("学生[{}]交卷 备注:sign=[{}]，被动交卷", studentId, sign);
        }
        else{
            LOGGER.info("学生[{}]交卷 备注:sign=[{}]，未知情况", studentId, sign);
        }
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
