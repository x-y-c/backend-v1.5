package yangchen.exam.service.compileService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.Score;
import yangchen.exam.entity.Submit;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.CompileFront;
import yangchen.exam.model.CompileModel;
import yangchen.exam.model.CompileResult;
import yangchen.exam.service.http.IOkhttpService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.score.ScoreService;
import yangchen.exam.service.submit.SubmitService;
import yangchen.exam.service.testInfo.TestCaseService;

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
    private IOkhttpService okhttpService;

    @Autowired
    private SubmitService submitService;

    @Autowired
    private ScoreService scoreService;


    public static Logger LOGGER = LoggerFactory.getLogger(CompileServiceImpl.class);

    @Override
    public CompileFront compileCode(Integer examinationId, Integer index, String src, Integer studentId) {

        double score = 0.0;
        int succCount = 0;

        QuestionNew questionBy = questionService.getQuestionBy(examinationId, index);
        submitService.addSubmit(Submit.builder().examinationId(examinationId).questionId(questionBy.getQuestionBh()).src(src).studentId(Long.valueOf(studentId)).build());
        List<TestCase> TestCaseList = testCaseService.findByQuestionId(questionBy.getQuestionBh());
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
            output.add("Hello World\n");
            input.add("");
        }
        compileModel.setInput(input);
        compileModel.setOutput(output);
        compileModel.setJudgeId(1);
        compileModel.setSrc(src);
        compileModel.setMemoryLimit(65535L);
        compileModel.setTimeLimit(1000L);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();
        Gson gson = gsonBuilder.create();
        String compileModelJson = gson.toJson(compileModel);
        LOGGER.info(compileModelJson);
        String jsonResult = okhttpService.postJsonBody("http://119.3.217.233:8080/judge.do", compileModelJson);
        LOGGER.info(jsonResult);
        CompileResult compileResult = gson.fromJson(jsonResult, CompileResult.class);
        if (compileResult.getResult() != null) {
//            for (Result result : compileResult.getResult()) {
            for (int i = 0; i < compileResult.getResult().size(); i++) {

                if (compileResult.getResult().get(i).getResult().equals("0") || compileResult.getResult().get(i).getResult().equals("1")) {
                    score = TestCaseList.get(i).getScoreWeight() + score;
                }
            }

        }

        Score score1 = new Score();
        score1.setExaminationId(examinationId);
        score1.setStudentId(studentId);
        score1.setScore((int) Math.round(score));
        score1.setIndex(index);
//        score1.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        scoreService.saveOrUpdate(score1);

        CompileFront compileFront = new CompileFront();
        compileFront.setCompileMsg(compileResult.getGlobalMsg());

        compileFront.setCompileSucc(compileResult.getGlobalMsg() == null);
        compileFront.setScore(score);

        return compileFront;
    }
}
