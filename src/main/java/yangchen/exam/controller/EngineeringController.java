package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.Enum.UserTypeEnum;
import yangchen.exam.entity.Administrator;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.adminManagement.AdminManagement;
import yangchen.exam.service.excelservice.EngineeringService;
import yangchen.exam.service.excelservice.EngineeringServiceImpl;
import yangchen.exam.service.teacher.TeacherService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = "/enginnering", produces = MediaType.APPLICATION_JSON_VALUE)
public class EngineeringController {

    @Autowired
    private EngineeringServiceImpl engineeringServiceimpl;

    @Autowired
    private EngineeringService engineeringService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AdminManagement adminManagement;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult uploadStudent(@RequestParam MultipartFile file, @RequestParam String teacherName) throws IOException {
        InputStream inputStream = file.getInputStream();
        return engineeringServiceimpl.huExcel(teacherName, inputStream);
    }

//    @RequestMapping(value = "/info", method = RequestMethod.GET)
//    public JsonResult getStudentInfo(@RequestParam(required = false) Integer studentId,@RequestParam String userName,String type) {
//        if(type.equals(UserTypeEnum.getUserTypeCode("学生"))){
//            return JsonResult.succResult(engineeringService.getStudentByStudentId(studentId));
//        }
//        else if(type.equals(UserTypeEnum.getUserTypeCode("教师"))){
//            Teacher teacher = teacherService.findTeacherByName(userName);
//            return JsonResult.succResult(teacher);
//        }
//        else{
//            Administrator administrator = adminManagement.findByAdminName(userName);
//            return JsonResult.succResult(administrator);
//        }
//    }
}

