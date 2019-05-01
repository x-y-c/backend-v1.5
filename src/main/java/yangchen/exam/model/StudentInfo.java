package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YC
 * @date 2019/5/1 21:17
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo {
    /**
     * 学号
     */
    private Long studentId;

    private String name;

    /**
     * 班级
     */
    private String grade;
    /**
     * 专业
     */
    private String major;
}
