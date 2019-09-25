package yangchen.exam.model;

/**
 * @author YC
 * @date 2019/4/11 15:52
 * O(∩_∩)O)
 */

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 定义http返回值的全局返回编码
 * 通过前端通过编码判断状态
 */
@Data
@AllArgsConstructor
public class ResultCode {
    /**
     * A0000 成功
     * A0001 教师登陆
     * A0002 管理员登陆
     * R0001 用户不存在
     * R0002 密码错误
     * R0003 参数错误
     * R0004 用户已经存在
     * R0005 网络错误
     * R0006 上传图片为空
     * R0007 考试已过期
     * R0008 没有权限
     * R0009 编译出错
     * R0010 测试用例权重不是100
     * R0011 没有上一题
     * R0012 没有下一题
     * R0013 上传为空
     * R0014 模块关闭
     */
    public static String SUCCESS = "A0000";
    public static String TEACHER_LOGIN = "A0001";
    public static String ADMIN_LOGIN = "A0002";
    public static String USER_NOT_EXIST = "R0001";
    public static String WRONG_PASSWORD = "R0002";
    public static String WRONG_PARAMS = "R0003";
    public static String USER_EXIST = "R0004";
    public static String NET_ERROR = "R0005";
    public static String FILE_UPLOAD_NULL = "R0006";
    public static String OVER_ENDTIME = "R0007";
    public static String NO_PERMISSION = "R0008";
    public static String COMPILE_ERROR = "R0009";
    public static String TESTCASE_SCORE_WEIGHT_ERROR = "R0010";
    public static String NO_FRONT_QUESTION = "R0011";
    public static String NO_NEXT_QUESTION = "R0012";
    public static String UPLOAD_NULL="R0013";
    public static String MODULE_CLOSE="R0014";

}
