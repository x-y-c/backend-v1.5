package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.ProjectGroup;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.model.*;
import yangchen.exam.service.project.ProjectService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.util.IpUtil;
import yangchen.exam.util.UserUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private static Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/showSelect", method = RequestMethod.GET)
    public JsonResult showSelect(@RequestParam String stage) {
        List<ClassModel> questionsAndId = questionService.getQuestionsAndId(stage);
        if(questionsAndId.size()==0){
            return JsonResult.errorResult(ResultCode.QUESTION_TYPE_ERROR,"此阶段没有习题","");
        }
        return JsonResult.succResult(questionsAndId);
    }


    @RequestMapping(value = "/homeworkInfo/complex",method = RequestMethod.POST)
    public JsonResult createProject(@RequestBody ProjectParam projectParam){
        ProjectGroup projectGroup = projectService.createProject(projectParam);
        return JsonResult.succResult(projectGroup);
    }

    @RequestMapping(value = "/homeworkInfo/homeworkGroup/page",method =RequestMethod.GET)
    public JsonResult getHomeworkInfo(@RequestParam Integer page, Integer pageLimit, String teacherName){
        Page<ProjectGroup> projectPage = projectService.getProjectPage(page-1, pageLimit, teacherName);
        return JsonResult.succResult(projectPage);
    }

    @RequestMapping(value = "/homeworkInfo/delete",method = RequestMethod.POST)
    public JsonResult deleteHomeworkInfo(@RequestParam Integer homeworkId){
        projectService.deleteProject(homeworkId);
        return JsonResult.succResult(null);
    }

    @RequestMapping(value = "/homeworkInfo/teacher/projectPaper",method = RequestMethod.GET)
    public JsonResult getExamPaperList(@RequestParam Integer homeworkGroupId){
        ProjectDetails projectDetails = projectService.getProjectDetails(homeworkGroupId);
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        projectDetailsList.add(projectDetails);
        return JsonResult.succResult(projectDetailsList);
    }

    @RequestMapping(value = "/homeworkInfo/teacher/getScore",method = RequestMethod.GET)
    public JsonResult getProjectScoreByTeacher(@RequestParam Integer homeworkGroupId){
        List<ProjectScoreModel> score = projectService.getScoreByTeacher(homeworkGroupId);
        return JsonResult.succResult(score);
    }

    @GetMapping(value = "/downScore")
    public void downloadScoreExcel(HttpServletResponse response, @RequestParam Integer homeworkGroupId) throws IOException {
        projectService.exportScore(response,homeworkGroupId);
    }

    @GetMapping("/downSubmit")
    public void downloadSubmit(HttpServletResponse response,@RequestParam Integer homeworkGroupId) throws IOException {
        projectService.exportSubmit(response,homeworkGroupId);
    }

    @GetMapping("/downSubmitAll")
    public void downloadSubmitAll(HttpServletResponse response,@RequestParam Integer homeworkGroupId)throws IOException{
        projectService.exportSubmitAll(response,homeworkGroupId);
    }

    @RequestMapping(value = "/homeworkInfo/detail",method = RequestMethod.GET)
    public JsonResult getProjectInfo(@RequestParam Integer studentId){
        List<ExaminationDetail> projectDetails = projectService.getProjectDetail(studentId);
        return JsonResult.succResult(projectDetails);
    }

    @RequestMapping(value = "/homeworkInfo/question", method = RequestMethod.GET)
    public JsonResult getQuestionInfo(Integer id, Integer studentId) {
        return projectService.getProjectPaper(id, studentId);
    }

    @RequestMapping(value = "/homeworkInfo/submit", method = RequestMethod.GET)
    public JsonResult submitExamPaper(@RequestParam Integer examInfoId,
                                      @RequestParam Integer studentId,
                                      @RequestParam(required = false) Integer sign){
        Boolean aBoolean = projectService.projectSubmitExamPaper(examInfoId, studentId, sign);
        return JsonResult.succResult(aBoolean);
    }

}
