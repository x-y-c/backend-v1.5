package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/4 20:57
 * O(∩_∩)O)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompileModel {

    /**
     * 测试用例的输入，如果有多个值，用空格分开；
     */
    private List input;
    /**
     * 测试用例的输出值，如果同一用例有多个值，用空格分开；
     */
    private List output;

    /**
     * 代码的时间限制，单位为毫秒；
     */
    private Long timeLimit;

    /**
     * 代码的内存限制
     */
    private Long memoryLimit;
    /**
     * 不同的编译器类型，默认为1，c语言
     */
    private Integer judgeId;
    /**
     * 代码；
     */
    private String src;
}
