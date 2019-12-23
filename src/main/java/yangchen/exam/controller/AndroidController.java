package yangchen.exam.controller;


import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.Enum.DifficultEnum;
import yangchen.exam.Enum.QuestionTypeEnum;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.QuestionSelectModel;
import yangchen.exam.model.ResultCode;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.student.StudentService;

import java.util.ArrayList;
import java.util.List;

@Api(value = "AndroidController")
@RestController
@RequestMapping(value = "/android", produces = MediaType.APPLICATION_JSON_VALUE)
public class AndroidController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private QuestionService questionService;

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonResult test(@RequestParam String httptest) {
          return JsonResult.succResult("connect success!");
    }

    @RequestMapping(value="/studentLogin",method = RequestMethod.POST)
    public JsonResult Login(@RequestParam String id, String pw){
        StudentNew student;
        try{
            student = studentService.getStudentByStudentId(Integer.valueOf(id));
        }catch (NumberFormatException e){
            LOGGER.info("学生登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",id,ResultCode.USER_NOT_EXIST,"用户不存在");
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
        }

        if (student == null) {
            LOGGER.info("学生登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",id,ResultCode.USER_NOT_EXIST,"用户不存在");
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
        }
        if (!student.getPassword().equals(pw)) {
            LOGGER.info("学生登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",id,ResultCode.WRONG_PASSWORD,"密码错误");
            return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "密码错误", null);
        }
        LOGGER.info("学生登陆：用户 [{}] 登陆成功",id);
        return JsonResult.succResult(student);
    }

    @RequestMapping(value = "/getQuestion", method = RequestMethod.GET)
    public JsonResult getQuestion(@RequestParam String questionType){
        try{
           questionType = QuestionTypeEnum.getQuestionTypeCode(questionType);
//           LOGGER.error("[{}]",questionType);
           if(questionType==null){
               return JsonResult.errorResult(ResultCode.QUESTION_TYPE_ERROR,"没有此类型的题目","");
           }
        }catch (Exception e){
            return JsonResult.errorResult(ResultCode.QUESTION_TYPE_ERROR,"没有此类型的题目","");
        }
        List<QuestionNew> questionNewList = questionService.getQuestion(questionType);
        List<QuestionSelectModel> questionSelectModels = new ArrayList<>();
        questionNewList.forEach(questionNew -> {
            QuestionSelectModel questionSelect = new QuestionSelectModel();
            questionSelect.setId(questionNew.getId());
            questionSelect.setAnswer(questionNew.getAnswer());
            questionSelect.setQuestionBh(questionNew.getQuestionBh());
            questionSelect.setQuestionDetails(questionNew.getQuestionDetails());
            questionSelect.setQuestionName(questionNew.getQuestionName());
            questionSelect.setStage(StageEnum.getStageName(questionNew.getStage()));
            questionSelect.setDifficulty(DifficultEnum.getDifficultName(questionNew.getDifficulty()));
            questionSelectModels.add(questionSelect);
        });
        return JsonResult.succResult(questionSelectModels);

    }
}
