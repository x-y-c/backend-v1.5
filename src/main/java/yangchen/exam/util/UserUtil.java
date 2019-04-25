package yangchen.exam.util;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {


    /**
     * 前端在请求的时候，添加header ，userId，这里是 userId的工具类，封装了获取用户id的方法；userId方便记录日志和其他的查询等
     * 针对用户的操作
     * @param httpServletRequest
     * @return
     */
    public static String getUserId(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("userId");
    }
}
