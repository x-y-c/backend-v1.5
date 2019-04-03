package yangchen.exam.service.biz;

import yangchen.exam.model.LoginResult;

public interface UserBizService {

    //学生登录
    LoginResult studentLogin(String studentId, String password);
    //教师登录
    LoginResult teacherLogin(String teacherEngName, String password);
}
