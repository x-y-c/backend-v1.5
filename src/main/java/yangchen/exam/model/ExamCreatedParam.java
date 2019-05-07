package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/7 14:28
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamCreatedParam {

    /**
     * category: 阶段，之后改成可以支持多个阶段的；
     * number： 题目数；默认为5道题；
     * grades： 班级列表，即本次有多少班级考试
     * start：开始时间，是时间戳格式；
     * end：结束时间，时间戳格式
     * ttl： 考试持续时间
     * desc： 考试描述；
     * <p>
     * 一次考试可能有多个班级，多个人，每个人对应一张考卷；
     */
    private String category;
    private Integer number;
    private List<String> grades;
    private Timestamp start;
    private Timestamp end;
    private Long ttl;
    private String desc;
}
