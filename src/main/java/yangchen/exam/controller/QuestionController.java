package yangchen.exam.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.Enum.DifficultEnum;
import yangchen.exam.Enum.QuestionTypeEnum;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.ModuleStatus;
import yangchen.exam.entity.QuestionLog;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.*;
import yangchen.exam.repo.ModuleStatusRepo;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.service.FileUpload.FileUpAndDownService;
import yangchen.exam.service.excelservice.ExcelServiceImpl;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.testInfo.TestCaseService;
import yangchen.exam.service.testInfo.TestInfoService;
import yangchen.exam.util.DecodeQuestionDetails;
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

    @Autowired
    private ModuleStatusRepo moduleStatusRepo;

    @Value("${image.nginx.url.path}")
    private String domainStr;

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
//        LOGGER.info("[{}] delete question, the ip = [{}]",
//                UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(null);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public JsonResult isQuestionChecked(@RequestParam String questionBh) {
        QuestionNew questionNew = questionRepo.findByQuestionBh(questionBh);
        questionNew.setActived(!questionNew.getActived());
        QuestionNew save = questionRepo.save(questionNew);
        String flag = "审核";
        QuestionLog questionLog = questionService.addQuestionLog(questionNew, flag);
        LOGGER.info("用户[{}]审核题目[{}],当前题目状态为[{}]",questionLog.getEditCustomBh(),questionLog.getQuestionBh(),questionNew.getActived());
        return JsonResult.succResult(save);
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
        questionById.setQuestionDetails(DecodeQuestionDetails.getRightImage(domainStr, questionById.getQuestionDetails()));
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
            LOGGER.info("定位/exam/question/image=[{}]",e.getMessage());
        }
        return JsonResult.errorResult(ResultCode.WRONG_PARAMS, "呜呜呜呜", returnMap);

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult uploadQuestion(@RequestBody QuestionUpdate questionUpdate) throws IOException {

        //LOGGER.info(questionNew.toString());
        /*修改sourceCode 格式*/
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        SourceCode sourceCode = new SourceCode();
        String code = questionUpdate.getSourceCode();
        List<SourceCodeInfo> key = new ArrayList<>();
        SourceCodeInfo sourceCodeInfo = new SourceCodeInfo();
        sourceCodeInfo.setCode(code);
        sourceCodeInfo.setFileName("main.c");
        key.add(sourceCodeInfo);
        sourceCode.setKey(key);
        String s = gson.toJson(sourceCode).toString();
        questionUpdate.setSourceCode(s);
        questionUpdate.setActived(Boolean.FALSE);
        if ("100001".equals(questionUpdate.getIsProgramBlank())) {
            questionUpdate.setMemo(questionUpdate.getMemo());
        }
        String flag = "";
        QuestionNew question = questionService.findByQuestionBh(questionUpdate.getQuestionBh());
        if (question != null) {
            //更新 id作为数据库主键，需要setID
            flag = "修改";
            questionUpdate.setId(question.getId());
            LOGGER.info("用户[{}]修改题目：question [{}]", question.getCustomBh(),question.getQuestionBh());
        } else {

            flag = "新增";
            TestCase testCase = new TestCase();
            testCase.setTestCaseBh(UUID.randomUUID().toString().replace("-", ""));
            testCase.setScoreWeight(0.0);
            String questionBh = UUID.randomUUID().toString().replace("-", "");
            testCase.setQuestionId(questionBh);
            TestCase testCase1 = testCaseService.addTestCase(testCase);
            questionUpdate.setQuestionBh(questionBh);
            LOGGER.info("新增题目question[{}]",question.getQuestionBh());
        }

        //QuestionNew questionResult = questionService.saveQuestionWithImgDecode(questionNew);
        QuestionNew questionResult = questionService.saveQuestionWithImgDecodeNew(questionUpdate);
        if (questionResult != null) {
            QuestionLog questionLog = questionService.addQuestionLog(questionUpdate, flag);
            LOGGER.info("questionLog.toString()===[{}]",questionLog.toString());
            return JsonResult.succResult(questionResult);
        } else {
            return JsonResult.errorResult(ResultCode.WRONG_PARAMS, "添加失败", null);
        }


    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public JsonResult searchStage(@RequestParam(required = false) String condition, @RequestParam(required = false) String value, int page, int pageLimit) {
//        LOGGER.info("[{}],[{}],[{}],[{}]", condition, value, page, pageLimit);
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
    @RequestMapping(value = "/testCaseAll", method = RequestMethod.POST)
    public JsonResult testCaseModify(@RequestParam String testCaseBh, @RequestParam Double scoreWeight, @RequestParam String testCaseInput, @RequestParam String testCaseOutput, @RequestParam String questionId, @RequestParam Integer operate) {
//        LOGGER.info("testCaseBh=[{}],scoreWeight=[{}],testCaseInput=[{}],testCaseOutput=[{}],questionId=[{}],operate=[{}]",
//                testCaseBh, scoreWeight, testCaseInput, testCaseOutput, questionId, operate);
        JsonResult jsonResult = testCaseService.modifyTestCase(testCaseBh, scoreWeight, testCaseInput, testCaseOutput, questionId, operate);
        return jsonResult;
    }

    @RequestMapping(value = "/testCase/reset", method = RequestMethod.GET)
    public void testReset() {
        testCaseService.resetList();
    }


    @RequestMapping(value = "/practice", method = RequestMethod.GET)
    public JsonResult getPracticeList() {
        if (moduleStatusRepo.findByModule("practice").getStatus().equals(Boolean.TRUE)) {
            List<QuestionPractice> result = new ArrayList<>();
            QuestionPractice questionPracticeInfo = questionService.getQuestionPracticeInfo();
            result.add(questionPracticeInfo);
            return JsonResult.succResult(result);
        } else {
            return JsonResult.errorResult(ResultCode.MODULE_CLOSE, "练习系统暂不开放", "");
        }

    }

    @RequestMapping(value = "/practiceItem", method = RequestMethod.GET)
    public JsonResult getPracticeItem(@RequestParam String questionBh, @RequestParam Integer studentId) {
        QuestionDetail questionDetail = questionService.getPracticeItem(questionBh, studentId);
        List<QuestionDetail> result = new ArrayList<>();
        result.add(questionDetail);
        return JsonResult.succResult(result);
    }

    @RequestMapping(value = "/practice/getFront", method = RequestMethod.GET)
    public JsonResult getPracticeFront(@RequestParam String questionBh) {
        String questionBhIWant = questionService.getPracticeFront(questionBh);
        if (StringUtils.isEmpty(questionBhIWant)) {
            return JsonResult.errorResult(ResultCode.NO_FRONT_QUESTION, "当前为第一题！", null);
        } else {
            return JsonResult.succResult(questionBhIWant);
        }

    }

    @RequestMapping(value = "/practice/getNext", method = RequestMethod.GET)
    public JsonResult getPracticeNext(@RequestParam String questionBh) {
        String questionBhIWant = questionService.getPracticeNext(questionBh);
        if (StringUtils.isEmpty(questionBhIWant)) {
            return JsonResult.errorResult(ResultCode.NO_NEXT_QUESTION, "当前为最后一题！", null);
        } else {
            return JsonResult.succResult(questionBhIWant);
        }
    }


    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public JsonResult getQuestionLog() {
        List<QuestionLogModel> questionLogModelList = questionService.getQuestionLog();

        return JsonResult.succResult(questionLogModelList);
    }

    @RequestMapping(value = "/getAnswer", method = RequestMethod.GET)
    public JsonResult getAnswer(@RequestParam String questionBh) {
        String answer = questionService.getAnswer(questionBh);

        return JsonResult.succResult(answer);
    }

    @RequestMapping(value = "/modifyPracticeStatus", method = RequestMethod.GET)
    public JsonResult modifyPracticeStatus() {
        ModuleStatus moduleStatus = moduleStatusRepo.findByModule("practice");
        moduleStatus.setStatus(!moduleStatus.getStatus());
        ModuleStatus save = moduleStatusRepo.save(moduleStatus);
        return JsonResult.succResult(save);
    }

    @RequestMapping(value = "/practiceStatus", method = RequestMethod.GET)
    public JsonResult practiceStatus() {
        ModuleStatus moduleStatus = moduleStatusRepo.findByModule("practice");
        return JsonResult.succResult(moduleStatus.getStatus());
    }

}
