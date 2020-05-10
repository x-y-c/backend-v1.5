package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.model.ClassModel;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.util.IpUtil;
import yangchen.exam.util.UserUtil;

import java.util.List;

@RestController
@RequestMapping(value = "/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private static Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/showSelect", method = RequestMethod.GET)
    public JsonResult showSelect(String stage) {
        List<ClassModel> questionsAndId = questionService.getQuestionsAndId(stage);
        if(questionsAndId.size()==0){
            return JsonResult.errorResult(ResultCode.QUESTION_TYPE_ERROR,"此阶段没有习题","");
        }
        return JsonResult.succResult(questionsAndId);
    }
}
