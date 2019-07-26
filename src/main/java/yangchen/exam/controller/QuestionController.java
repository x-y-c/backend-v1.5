package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.excelservice.ExcelServiceImpl;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.testInfo.TestCaseService;
import yangchen.exam.service.testInfo.TestInfoService;
import yangchen.exam.util.IpUtil;
import yangchen.exam.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JsonResult createQuestion(@RequestBody QuestionNew question) {
        QuestionNew questionResult = questionService.createQuestion(question);
        LOGGER.info("[{}] create question, the ip = [{}]",
                UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionResult);
    }



    @RequestMapping(value = "/questionId", method = RequestMethod.GET)
    public JsonResult findQuestionById(@RequestParam String id) {
        QuestionNew questionById = questionService.findByQuestionBh(id);
        LOGGER.info("[{}] find question by Id,the ip = [{}]", UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionById);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JsonResult DeleteQuestionById(@RequestBody Integer id) {
        Boolean aBoolean = questionService.deleteQuestion(id);
        if (aBoolean) {
            LOGGER.info("[{}] delete question by id, the ip = [{}]", UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
            return JsonResult.succResult(null);
        }
        return JsonResult.errorResult("fail", "删除失败", null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult updateQuestion(@RequestBody QuestionNew question) {
        QuestionNew questionResult = questionService.updateQuestion(question);
        LOGGER.info("[{} ]update question , the ip = [{}]", UserUtil.getUserId(httpServletRequest), IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionResult);

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
        List<QuestionNew> questionList = questionService.findQuestionAll();
        return JsonResult.succResult(questionList);
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public JsonResult getPagedQuestion(int pageNo,int pageLimit){
        Page<QuestionNew> pageQuestion = questionService.getPageQuestion(pageNo - 1, pageLimit);
        return JsonResult.succResult(pageQuestion);
    }


    @RequestMapping(value = "/testCase", method = RequestMethod.GET)
    public JsonResult getTestCase(@RequestParam String questionId) {
        List<TestCase> byQid = testCaseService.findByQuestionId(questionId);


        return JsonResult.succResult(byQid);

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


}
