package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.Student;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.base.studentService;
import yangchen.exam.util.UserUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YC
 * @date 2019/4/17 17:03
 * O(∩_∩)O)
 */

@RestController
@RequestMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentInfoController {
    @Autowired
    private studentService studentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentInfoController.class);


    @Autowired
    private HttpServletRequest request;

    //查询所有学生；
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JsonResult getAllStudent() {
        LOGGER.info("[{}] 查询全部学生", request.getHeader("userId"));
        return JsonResult.succResult(studentService.getAllStudent());
    }


    @RequestMapping(value = "/major", method = RequestMethod.GET)
    public JsonResult getStudentByMajor(@RequestParam String major) {
        LOGGER.info("[{}] get student By Major", UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.getStudentByMajor(major));
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult AddStudent(@RequestBody Student student) {
        LOGGER.info("[{}] add student",UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.addStudent(student));
    }


    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public JsonResult updateStudent(@RequestBody Student student) {
        LOGGER.info("[{}] update student",UserUtil.getUserId(request));
        return JsonResult.succResult(studentService.changeStudentInfo(student));
    }
}
