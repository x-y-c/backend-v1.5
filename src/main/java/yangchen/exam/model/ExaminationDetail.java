package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author YC
 * @date 2019/5/7 17:16
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationDetail {

    /**
     * 考试类型，显示阶段信息；给学生显示
     */
    private String category;
    /**
     * 显示开始时间；
     */
    private Timestamp start;
    /**
     * 显示结束时间
     */
    private Timestamp end;
    /**
     * 显示描述
     */
    private String desc;
    /**
     * 考试时间
     */
    private Long ttl;
}
