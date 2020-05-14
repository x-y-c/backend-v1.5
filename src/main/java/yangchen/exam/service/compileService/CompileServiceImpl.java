package yangchen.exam.service.compileService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.*;
import yangchen.exam.model.CompileFront;
import yangchen.exam.model.CompileModel;
import yangchen.exam.model.CompileResult;
import yangchen.exam.repo.ProjectInfoRepo;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.repo.SubmitPracticeRepo;
import yangchen.exam.service.http.OkhttpService;
import yangchen.exam.service.project.ProjectService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.score.ScoreService;
import yangchen.exam.service.submit.SubmitService;
import yangchen.exam.service.testInfo.TestCaseService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/15 13:48
 * O(∩_∩)O)
 */
@Service
public class CompileServiceImpl implements CompileService {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private OkhttpService okhttpService;

    @Autowired
    private SubmitService submitService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private SubmitPracticeRepo submitPracticeRepo;

    @Autowired
    private ProjectInfoRepo projectInfoRepo;

    @Autowired
    private ProjectService projectService;

    @Value("${compile.url.path}")
    private String compileUrl;

    public static Logger LOGGER = LoggerFactory.getLogger(CompileServiceImpl.class);

    @Override
    public CompileFront compileCode(Integer examinationId, Integer index, String src, Integer studentId, String questionBh) {
        boolean isPractice = false;
        double score = 0.0;
        int succCount = 0;
        List<TestCase> TestCaseList = new ArrayList<>();
        QuestionNew questionBy = null;
        Boolean isChangeSrc = false;
        SubmitPractice submitPractice = new SubmitPractice();



        if (StringUtils.isEmpty(questionBh)) {
            questionBy = questionService.getQuestionBy(examinationId, index);
            if(questionBy.getIsProgramBlank().equals("100001")){
                if(questionService.isChangeSrc(questionBy,src)){
                    return null;
                }
            }
            submitService.addSubmit(
                    Submit.builder()
                            .examinationId(examinationId)
                            .questionId(questionBy.getQuestionBh())
                            .src(src)
                            .studentId(Long.valueOf(studentId))
                    .build());
        }
        else {
            //练习的情况
            isPractice = true;
            questionBy = questionRepo.findByQuestionBh(questionBh);
            if(questionBy.getIsProgramBlank().equals("100001")){
                if(questionService.isChangeSrc(questionBy,src)){
                    return null;
                }
            }
            //todo
            //需要一张记录练习题的表嘛？yes
            submitPractice.setQuestionId(String.valueOf(questionBy.getId()));
            submitPractice.setSrc(src);
            submitPractice.setSubmitTime(new Timestamp(System.currentTimeMillis()));
            submitPractice.setStudentId(studentId);
        }
        TestCaseList = testCaseService.findByQuestionId(questionBy.getQuestionBh());
        CompileModel compileModel = new CompileModel();
        List<String> input = new ArrayList<>();
        List<String> output = new ArrayList<>();

        //做了判空操作，保证测试用例不存在的情况
        if (TestCaseList != null && TestCaseList.size() > 0) {
            for (TestCase t : TestCaseList) {
                input.add(t.getTestCaseInput());
                output.add(t.getTestCaseOutput());
            }
        } else {
            output.add("");
            input.add("");
        }
        compileModel.setInput(input);
        compileModel.setOutput(output);
        compileModel.setJudgeId(1);
        compileModel.setSrc(src);
        compileModel.setMemoryLimit(65535L);
        compileModel.setTimeLimit(1000L);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        gsonBuilder.disableHtmlEscaping();
        String compileModelJson = gson.toJson(compileModel);
        LOGGER.info("学生[{}]提交，compileModelJson=[{}]",studentId,compileModelJson);
        //todo  /*jsonResult 如果报错会返回null，需要再这里加一些处理*/
        String jsonResult = okhttpService.postJsonBody(studentId,compileUrl, compileModelJson);
        LOGGER.info("学生[{}]提交，jsonResult=[{}]",studentId,jsonResult);
        CompileResult compileResult = gson.fromJson(jsonResult, CompileResult.class);
        LOGGER.info("学生[{}]提交，compileResult=[{}]",studentId,compileResult);
        if (compileResult.getResult() != null) {
            // for (Result result : compileResult.getResult()) {
            for (int i = 0; i < compileResult.getResult().size(); i++) {
                if (compileResult.getResult().get(i).getResult().equals("0") || compileResult.getResult().get(i).getResult().equals("1")) {
                    score = TestCaseList.get(i).getScoreWeight() + score;
                }
            }
        }

        if (isPractice) {
            submitPractice.setScore((int) Math.round(score));
            submitPracticeRepo.save(submitPractice);
        } else {
            Score score1 = new Score();
            score1.setExaminationId(examinationId);
            score1.setStudentId(studentId);
            score1.setScore((int) Math.round(score));
            score1.setIndex(index);
            score1.setQuestionId(questionBy.getQuestionBh());
            scoreService.saveOrUpdate(score1);
        }

        CompileFront compileFront = new CompileFront();
        compileFront.setCompileMsg(compileResult.getGlobalMsg());

        compileFront.setCompileSucc(compileResult.getGlobalMsg() == null);
        compileFront.setScore(score);

        return compileFront;
    }

    @Override
    public CompileFront compileProject(Integer projectInfoId, Integer index, String src, Integer studentId) {
        double score = 0.0;
        List<TestCase> TestCaseList = new ArrayList<>();
        List<QuestionNew> questionNewList = new ArrayList<>();

        Boolean isChangeSrc = false;

        ProjectInfo projectInfo = projectInfoRepo.findById(projectInfoId).get();

        questionNewList = questionService.getProjectPaperByProjectInfoId(projectInfoId);


        QuestionNew question = questionNewList.get(index);

        if("100001".equals(question.getIsProgramBlank())){
            if(questionService.isChangeSrc(question,src)){
                return null;
            }
        }

        TestCaseList = testCaseService.findByQuestionId(question.getQuestionBh());
        CompileModel compileModel = new CompileModel();
        List<String> input = new ArrayList<>();
        List<String> output = new ArrayList<>();

        //做了判空操作，保证测试用例不存在的情况
        if (TestCaseList != null && TestCaseList.size() > 0) {
            for (TestCase t : TestCaseList) {
                input.add(t.getTestCaseInput());
                output.add(t.getTestCaseOutput());
            }
        } else {
            output.add("");
            input.add("");
        }
        compileModel.setInput(input);
        compileModel.setOutput(output);
        compileModel.setJudgeId(1);
        compileModel.setSrc(src);
        compileModel.setMemoryLimit(65535L);
        compileModel.setTimeLimit(1000L);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        gsonBuilder.disableHtmlEscaping();
        String compileModelJson = gson.toJson(compileModel);
        LOGGER.info("学生[{}]提交，compileModelJson=[{}]",studentId,compileModelJson);
        String jsonResult = okhttpService.postJsonBody(studentId,compileUrl, compileModelJson);
        LOGGER.info("学生[{}]提交，jsonResult=[{}]",studentId,jsonResult);
        CompileResult compileResult = gson.fromJson(jsonResult, CompileResult.class);
        LOGGER.info("学生[{}]提交，compileResult=[{}]",studentId,compileResult);
        if (compileResult.getResult() != null) {
            // for (Result result : compileResult.getResult()) {
            for (int i = 0; i < compileResult.getResult().size(); i++) {
                if (compileResult.getResult().get(i).getResult().equals("0") || compileResult.getResult().get(i).getResult().equals("1")) {
                    score = TestCaseList.get(i).getScoreWeight() + score;
                }
            }
        }

        CompileFront compileFront = new CompileFront();
        compileFront.setCompileMsg(compileResult.getGlobalMsg());

        compileFront.setCompileSucc(compileResult.getGlobalMsg() == null);
        compileFront.setScore(score);

        Integer linesCount = getCodeLines(src);

        projectService.addSubmit(
                ProjectSubmit.builder()
                        .examPaperId(projectInfo.getProjectPaperId())
                        .questionIndex(index)
                        .src(src)
                        .codeLines(linesCount)
                        .studentId(studentId)
                        .score((int) Math.round(score))
                        .build()
        );

        return compileFront;
    }

    public Integer getCodeLines(String code){
        code = code.replace(" ","");
        code = code.replace("\t","");
        code = code.replace("\r","");

        int linesCount = 0;
        int index = -1;
        for(int i=0; i<code.length(); i++){
            if(Integer.valueOf(code.charAt(i))==10){
                if(i==0){
                    index = i;
                    continue;
                }
                if (index + 1 == i){
                    continue;
                }
                index = i;
                linesCount++;
            }
            else{
                if(i ==code.length()-1){
                    linesCount++;
                }
            }
        }
        return linesCount;
    }
}
