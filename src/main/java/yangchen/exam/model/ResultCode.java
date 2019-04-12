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
     * R0001 用户不存在
     * R0002 密码错误
     * R0003 没有权限
     */
    public  static String SUCCESS = "A0000";
    public static String USER_NOT_EXIST = "R0001";
    public  static String WRONG_PASSWORD="R0002";
}
