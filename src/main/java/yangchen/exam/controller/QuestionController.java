package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.entity.Question;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.Category;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.base.QuestionService;
import yangchen.exam.service.biz.TestInfoService;

import java.util.List;

/**
 * @Author: YC
 * @Date: 2019/4/8 11:18
 * O(∩_∩)O)
 */

@RestController
@RequestMapping(value = "/exam/question", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestInfoService testInfoService;

    public JsonResult createQuestion(@RequestBody Question question) {
        Question questionResult = questionService.createQuestion(question);
        return JsonResult.succResult(questionResult);
    }


    public JsonResult findQuestionByCategory(@RequestBody Category category) {
        List<Question> questionByCategory = questionService.findQuestionByCategory(category);
        return JsonResult.succResult(questionByCategory);
    }

    public JsonResult findQuestionById(@RequestBody Integer id) {
        Question questionById = questionService.findQuestionById(id);
        return JsonResult.succResult(questionById);
    }

    public JsonResult DeleteQuestionById(@RequestBody Integer id) {
        Boolean aBoolean = questionService.deleteQuestion(id);
        if (aBoolean) {
            return JsonResult.succResult(null);
        }
        return JsonResult.errorResult("fail", "删除失败", null);
    }

    public JsonResult updateQuestion(@RequestBody Question question) {
        Question questionResult = questionService.updateQuestion(question);
        return JsonResult.succResult(questionResult);

    }

    /**
     * //通过题目查看测试用例
     * //删除题目的时候，要连同测试用例一起删除
     *
     * @param id
     * @return
     */
    public JsonResult getTestCaseByQuestionId(@RequestBody Integer id) {
        List<TestCase> testCaseByQuestionId = testInfoService.getTestCaseByQuestionId(id);
        return JsonResult.succResult(testCaseByQuestionId);

    }


}
