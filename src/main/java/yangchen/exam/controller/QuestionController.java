package yangchen.exam.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.Enum.DifficultEnum;
import yangchen.exam.Enum.QuestionTypeEnum;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.*;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.service.FileUpload.FileUpAndDownService;
import yangchen.exam.service.excelservice.ExcelServiceImpl;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.testInfo.TestCaseService;
import yangchen.exam.service.testInfo.TestInfoService;
import yangchen.exam.util.DecodeSourceCode;
import yangchen.exam.util.IpUtil;
import yangchen.exam.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author: YC
 * @Date: 2019/4/8 11:18
 * O(∩_∩)O)
 */

@RestController
@RequestMapping(value = "/exam/question", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionController {

    private static Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private TestInfoService testInfoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ExcelServiceImpl excelService;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private FileUpAndDownService fileUpAndDownService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JsonResult createQuestion(@RequestBody QuestionNew question) {
        QuestionNew questionResult = questionService.createQuestion(question);
        LOGGER.info("[{}] create question, the ip = [{}]",
                UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionResult);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JsonResult deleteQuestionByQuestionBh(@RequestParam String questionBh) {
        questionService.deleteQuestion(questionBh);
        return JsonResult.succResult(null);
    }


    @RequestMapping(value = "/questionId", method = RequestMethod.GET)
    public JsonResult findQuestionById(@RequestParam String id) {
        QuestionNew questionById = questionService.findByQuestionBh(id);
        questionById.setStage(StageEnum.getStageName(questionById.getStage()));
        questionById.setDifficulty(DifficultEnum.getDifficultName(questionById.getDifficulty()));
        questionById.setQuestionType(QuestionTypeEnum.getQuestionTypeName(questionById.getQuestionType()));
        if (!StringUtils.isEmpty(questionById.getSourceCode())) {
            questionById.setSourceCode(DecodeSourceCode.getCode(questionById.getSourceCode()));
        }
//        LOGGER.info("StageEnum.getStageName(questionById.getStage())", StageEnum.getStageName(questionById.getStage()));
        //LOGGER.info("[{}] find question by Id,the ip = [{}]", UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionById);
    }


    /**
     * //通过题目查看测试用例
     * //删除题目的时候，要连同测试用例一起删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/testInfo", method = RequestMethod.GET)
    public JsonResult getTestCaseByQuestionId(@RequestParam String id) {
        List<TestCase> testCaseByQuestionId = testInfoService.getTestCaseByQuestionId(id);
        LOGGER.info("[{}] get testCaseBy questionId, the ip = [{}]", UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(testCaseByQuestionId);

    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JsonResult getAllQuestion() {
        List<QuestionNew> questionList = questionRepo.findByActivedIsTrue();
        return JsonResult.succResult(questionList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult getPagedQuestion(int page, int pageLimit) {
        Page<QuestionNew> pageQuestion = questionService.getPageQuestion(page - 1, pageLimit);
        return JsonResult.succResult(pageQuestion);
    }


    @RequestMapping(value = "/testCase", method = RequestMethod.GET)
    public JsonResult getTestCase(@RequestParam String questionId) {
        List<TestCase> byQid = testCaseService.findByQuestionId(questionId);
        List<TestCaseModel> result = new ArrayList<>(byQid.size());
        byQid.forEach(testCase -> {
            result.add(new TestCaseModel(testCase));
        });
        return JsonResult.succResult(result);
    }

    /**
     * 添加测试用例
     *
     * @param testCase
     * @return
     */
    @RequestMapping(value = "/testCase/add", method = RequestMethod.POST)
    public JsonResult addTestCase(@RequestBody TestCase testCase) {
        TestCase testCase1 = testCaseService.addTestCase(testCase);
        return JsonResult.succResult(testCase1);
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult uploadQuestion(@RequestParam MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        JsonResult jsonResult = excelService.uploadQuestion(inputStream);
        return JsonResult.succResult(jsonResult);
    }


    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public JsonResult imageUpload(@RequestParam MultipartFile file1) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            if (!file1.isEmpty()) {
                Map<String, Object> picMap = fileUpAndDownService.uploadPicture(file1);
                if (ResultCode.SUCCESS.equals(picMap.get("result"))) {
                    return JsonResult.succResult(picMap);
                } else {
                    returnMap.put("result", ResultCode.NET_ERROR);
                    returnMap.put("msg", picMap.get("result"));
                }
            } else {
                LOGGER.info(">>>上传的图片为空文件");
                returnMap.put("result", ResultCode.NET_ERROR);
                returnMap.put("msg", ResultCode.FILE_UPLOAD_NULL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
        return JsonResult.errorResult(ResultCode.WRONG_PARAMS, "呜呜呜呜", returnMap);

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult uploadQuestion(@RequestBody QuestionNew questionNew) throws IOException {

//        LOGGER.info(questionNew.toString());
       /*
       修改sourceCode 格式
        */
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        SourceCode sourceCode = new SourceCode();
        String code = questionNew.getSourceCode();
        List<SourceCodeInfo> key = new ArrayList<>();
        SourceCodeInfo sourceCodeInfo = new SourceCodeInfo();
        sourceCodeInfo.setCode(code);
        sourceCodeInfo.setFileName("main.c");
        key.add(sourceCodeInfo);
        sourceCode.setKey(key);
        String s = gson.toJson(sourceCode).toString();
        questionNew.setSourceCode(s);
        questionNew.setActived(Boolean.TRUE);

        QuestionNew question = questionService.findByQuestionBh(questionNew.getQuestionBh());
        if (question != null) {
            //更新
            questionNew.setId(question.getId());
        } else {
            TestCase testCase = new TestCase();
            testCase.setQuestionId(questionNew.getQuestionBh());
            testCase.setTestCaseBh(UUID.randomUUID().toString().replace("-", ""));
            testCase.setScoreWeight(0.0);
            TestCase testCase1 = testCaseService.addTestCase(testCase);
            String questionBh = UUID.randomUUID().toString().replace("-", "");
            questionNew.setQuestionBh(questionBh);
        }

        QuestionNew questionResult = questionService.saveQuestionWithImgDecode(questionNew);
        if (questionResult != null) {
            return JsonResult.succResult(null);
        } else {
            return JsonResult.errorResult(ResultCode.WRONG_PARAMS, "添加失败", null);
        }
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public JsonResult searchStage(@RequestParam(required = false) String condition, @RequestParam(required = false) String value, int page, int pageLimit) {
        LOGGER.info("[{}],[{}],[{}],[{}]", condition, value, page, pageLimit);
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(condition)) {
            Page<QuestionNew> pageQuestion = questionService.getPageQuestion(page - 1, pageLimit);
            return JsonResult.succResult(pageQuestion);
        } else if (condition.equals("阶段")) {
            Page<QuestionNew> stageQuestionPage = questionService.getStageQuestionPage(value, page - 1, pageLimit);
            return JsonResult.succResult(stageQuestionPage);
        } else if (condition.equals("题号")) {
            Page<QuestionNew> idQuestionPage = questionService.getIdQuestionPage(value, page - 1, pageLimit);
            return JsonResult.succResult(idQuestionPage);
        } else if (condition.equals("题目")) {
            Page<QuestionNew> titleQuestionPage = questionService.getTitleQuestionPage(value, page - 1, pageLimit);
            return JsonResult.succResult(titleQuestionPage);
        } else if (condition.equals("出题人")) {
            Page<QuestionNew> customBhQuestionPage = questionService.getCustomBhQuestionPage(value, page - 1, pageLimit);
            return JsonResult.succResult(customBhQuestionPage);
        } else {

            return JsonResult.succResult(null);
        }
    }

    /*
        private String testCaseBh;
        private Double scoreWeight;
        private String testCaseInput;
         private String testCaseOutput;
         private String testCaseTips;
        private String questionId;
        private String memo;
     */
    @RequestMapping(value = "/testCaseAll",method = RequestMethod.POST)
    public JsonResult testCaseModify(@RequestParam String testCaseBh,
                                     @RequestParam Double scoreWeight,
                                     @RequestParam String testCaseInput,
                                     @RequestParam String testCaseOutput,
                                     @RequestParam String questionId,
                                     @RequestParam Integer operate) {
        LOGGER.info("testCaseBh=[{}],scoreWeight=[{}],testCaseInput=[{}],testCaseOutput=[{}],questionId=[{}],operate=[{}]",
                testCaseBh, scoreWeight, testCaseInput, testCaseOutput, questionId, operate);
        JsonResult jsonResult = testCaseService.modifyTestCase(testCaseBh, scoreWeight, testCaseInput, testCaseOutput, questionId, operate);
        return jsonResult;
    }

    @RequestMapping(value = "/testCase/reset", method = RequestMethod.GET)
    public void testReset() {
        testCaseService.resetList();
    }

}
