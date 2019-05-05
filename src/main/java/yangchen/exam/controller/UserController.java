package yangchen.exam.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.UserBaseInfo;
import yangchen.exam.service.teacher.TeacherService;
import yangchen.exam.service.student.studentService;
import yangchen.exam.service.token.TokenService;
import yangchen.exam.util.IpUtil;
import yangchen.exam.util.JavaJWTUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yc
 */
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private studentService StudentService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private HttpServletRequest request;


    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PassToken
    @RequestMapping(value = "student/login", method = RequestMethod.GET)
    public JsonResult login(@RequestParam String studentId, @RequestParam String password) {
        Student student = StudentService.getStudentByStudentId(Long.valueOf(studentId));
        LOGGER.info("the ip is [{}]", IpUtil.getIpAddr(request));

        String userId = request.getHeader("userId");
        LOGGER.info("the userId is [{}]", userId);
        if (student == null) {
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
        }
        if (!student.getPassword().equals(password)) {
            return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "密码错误", null);
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
