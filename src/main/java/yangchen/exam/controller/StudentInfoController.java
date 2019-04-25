package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.Student;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.base.studentService;

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


    //查询所有学生；
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public JsonResult getAllStudent() {
        return JsonResult.succResult(studentService.getAllStudent());
    }


    @RequestMapping(value = "/major", method = RequestMethod.GET)
    public JsonResult getStudentByMajor(@RequestParam String major) {
        return JsonResult.succResult(studentService.getStudentByMajor(major));
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult AddStudent(@RequestBody Student student) {
        return JsonResult.succResult(studentService.addStudent(student));
    }


    public JsonResult updateStudent(@RequestBody Student student) {
        return JsonResult.succResult(studentService.changeStudentInfo(student));
    }
}
