package yangchen.exam.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.annotation.PassToken;
import yangchen.exam.annotation.UserLoginToken;
import yangchen.exam.entity.Student;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.UserBaseInfo;
import yangchen.exam.service.base.TeacherService;
import yangchen.exam.service.base.studentService;
import yangchen.exam.service.biz.TokenService;
import yangchen.exam.util.JavaJWTUtil;

/**
 * @author yc
 */
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private studentService StudentService;

    @Autowired
    private TokenService tokenService;


    @Autowired
    private TeacherService teacherService;

    @PassToken
    @RequestMapping(value = "student/login", method = RequestMethod.GET)
    public JsonResult login(@RequestParam String studentId, @RequestParam String password) {
        Student student = StudentService.getStudentByStudentId(Long.valueOf(studentId));
        if (student == null) {
            return JsonResult.errorResult("fail", "用户不存在", null);
        }
        if (!student.getPassword().equals(password)) {
            return JsonResult.errorResult("fail", "密码错误", null);
        }
        String token = tokenService.getToken(student);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setToken(token);
        userBaseInfo.setUserName(student.getName());
        return JsonResult.succResult("成功", userBaseInfo);
    }

    @RequestMapping(value = "teacher/login", method = RequestMethod.GET)
    public JsonResult teacherLogin(@RequestParam String teacherName, @RequestParam String password) {
        Teacher teacherByName = teacherService.findTeacherByName(teacherName);
        if (teacherByName == null) {
            return JsonResult.errorResult("fail", "用户不存在", null);
        }
        if (!teacherByName.getPassword().equals(password)) {
            return JsonResult.errorResult("fail", "密码错误", null);
        }
        String token = JavaJWTUtil.createTokenWithClaim(teacherName, password);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setToken(token);
        userBaseInfo.setUserName(teacherName);
        return JsonResult.succResult("成功", userBaseInfo);


    }

    @UserLoginToken
    @RequestMapping(value = "/getMessage")
    public JsonResult getMessage() {
        return JsonResult.succResult("验证成功", null);
    }
}
