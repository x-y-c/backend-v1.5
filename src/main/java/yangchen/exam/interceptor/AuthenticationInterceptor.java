package yangchen.exam.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import yangchen.exam.annotation.PassToken;
import yangchen.exam.annotation.UserLoginToken;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.TokenInfo;
import yangchen.exam.service.student.studentService;
import yangchen.exam.util.JavaJWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private studentService studentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader("token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                if (token == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                TokenInfo tokenInfo = JavaJWTUtil.verifyToken(token);
                Integer studentId = tokenInfo.getUserId();
                StudentNew student = studentService.getStudentByStudentId(studentId);
                if (student == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                return true;
                }

        }
            return true;
    }



    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

