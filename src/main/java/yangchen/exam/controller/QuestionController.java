package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.Question;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.Category;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.base.QuestionService;
import yangchen.exam.service.biz.TestInfoService;
import yangchen.exam.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
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
    private TestInfoService testInfoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JsonResult createQuestion(@RequestBody Question question) {
        Question questionResult = questionService.createQuestion(question);
        LOGGER.info("create question, the ip = [{}]", IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionResult);
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public JsonResult findQuestionByCategory(@RequestParam Category category) {
        List<Question> questionByCategory = questionService.findQuestionByCategory(category);
        LOGGER.info("find question by category, the ip = [{}]", IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionByCategory);
    }

    @RequestMapping(value = "/questionId", method = RequestMethod.GET)
    public JsonResult findQuestionById(@RequestParam Integer id) {
        Question questionById = questionService.findQuestionById(id);
        LOGGER.info("find question by Id,the ip = [{}]", IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(questionById);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public JsonResult DeleteQuestionById(@RequestBody Integer id) {
        Boolean aBoolean = questionService.deleteQuestion(id);
        if (aBoolean) {
            LOGGER.info("delete question by id, the ip = [{}]", IpUtil.getIpAddr(httpServletRequest));
            return JsonResult.succResult(null);
        }
        return JsonResult.errorResult("fail", "删除失败", null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult updateQuestion(@RequestBody Question question) {
        Question questionResult = questionService.updateQuestion(question);
        LOGGER.info("update question , the ip = [{}]", IpUtil.getIpAddr(httpServletRequest));
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
    public JsonResult getTestCaseByQuestionId(@RequestParam Integer id) {
        List<TestCase> testCaseByQuestionId = testInfoService.getTestCaseByQuestionId(id);
        LOGGER.info("get testCaseBy questionId, the ip = [{}]", IpUtil.getIpAddr(httpServletRequest));
        return JsonResult.succResult(testCaseByQuestionId);

    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JsonResult getAllQuestion() {
        List<Question> questionList = questionService.findQuestionAll();
        return JsonResult.succResult(questionList);

    }


}
