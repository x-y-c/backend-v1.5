package yangchen.exam.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yangchen.exam.model.JsonResult;


/**
 * @author yc
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public Object handleException(Exception e) {
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        e.printStackTrace();
        LOGGER.info("定位:GlobalExceptionHandler=",msg);
        return JsonResult.errorResult("fail", msg, null);
    }
}
