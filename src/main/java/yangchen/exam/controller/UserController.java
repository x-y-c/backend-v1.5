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
import yangchen.exam.entity.Administrator;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.UserBaseInfo;
import yangchen.exam.repo.AdministratorRepo;
import yangchen.exam.repo.IpAddrRepo;
import yangchen.exam.service.UA.UAService;
import yangchen.exam.service.teacher.TeacherService;
import yangchen.exam.service.token.TokenService;
import yangchen.exam.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author yc
 */
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private yangchen.exam.service.student.StudentService StudentService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UAService uaService;

    @Autowired
    private IpAddrRepo ipAddrRepo;

    @Autowired
    private AdministratorRepo administratorRepo;

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PassToken
    @RequestMapping(value = "student/login", method = RequestMethod.GET)
    public JsonResult login(@RequestParam String studentId, @RequestParam String password) {

//        LOGGER.info("the ip is [{}]", IpUtil.getIpAddr(request));
//        String userId = request.getHeader("userId");
//        LOGGER.info("the userId is [{}]", userId);
        StudentNew student;
        try{
            student = StudentService.getStudentByStudentId(Integer.valueOf(studentId));
        }catch (NumberFormatException e){
            LOGGER.info("学生登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",studentId,ResultCode.USER_NOT_EXIST,"用户不存在");
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
        }

        if (student == null) {
            LOGGER.info("学生登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",studentId,ResultCode.USER_NOT_EXIST,"用户不存在");
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
        }
        if (!student.getPassword().equals(password)) {
            LOGGER.info("学生登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",studentId,ResultCode.WRONG_PASSWORD,"密码错误");
            return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "密码错误", null);
        }
        String token = tokenService.getToken(student);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setToken(token);
        userBaseInfo.setUserName(student.getStudentName());
        LOGGER.info("学生登陆：用户 [{}] 登陆成功",studentId);
        return JsonResult.succResult("成功", userBaseInfo);
    }

    @RequestMapping(value = "teacher/login", method = RequestMethod.GET)
    public JsonResult teacherLogin(@RequestParam String teacherName, @RequestParam String password) {
        Teacher teacherByName = teacherService.findTeacherByName(teacherName);
        Administrator administrator = administratorRepo.findByAdminNameAndActived(teacherName, Boolean.TRUE);

        UserBaseInfo userBaseInfo = new UserBaseInfo();

        if (teacherByName != null) {
            if (password.equals(teacherByName.getPassword())) {
                userBaseInfo.setUserName(String.valueOf(teacherByName.getId()));
                LOGGER.info("管理员登陆：用户 [{}] 登陆成功",teacherName);
                return JsonResult.succResult(ResultCode.TEACHER_LOGIN, "登录成功", null);
            } else {
                LOGGER.info("管理员登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",teacherName,ResultCode.WRONG_PASSWORD,"密码错误");
                return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "密码错误", null);
            }
        }
        if (administrator != null) {
            if (password.equals(administrator.getAdminPassword())) {
                LOGGER.info("管理员登陆：用户 [{}] 登陆成功",teacherName);
                return JsonResult.succResult(ResultCode.ADMIN_LOGIN, "登录成功", null);
            } else {
                LOGGER.info("管理员登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",teacherName,ResultCode.WRONG_PASSWORD,"密码错误");
                return JsonResult.errorResult(ResultCode.WRONG_PASSWORD, "密码错误", null);
            }
        } else {
            LOGGER.info("管理员登陆：用户 [{}] 登陆失败--错误码：[{}]，错误原因：[{}]",teacherName,ResultCode.USER_NOT_EXIST,"用户不存在");
            return JsonResult.errorResult(ResultCode.USER_NOT_EXIST, "用户不存在", null);
        }
    }

    @UserLoginToken
    @RequestMapping(value = "/getMessage")
    public JsonResult getMessage() {
        return JsonResult.succResult("验证成功", null);
    }


    @RequestMapping(value = "/testUA/xy", method = RequestMethod.GET)
    public void test() throws IOException {

        String header = request.getHeader("User-Agent");
        uaService.testUa(header);
    }
}
