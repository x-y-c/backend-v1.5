package yangchen.exam.model;

/**
 * @author YC
 * @date 2019/5/21 23:05
 * O(∩_∩)O)
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 试题的详细信息，如果已经完成作答，那么used的值为1，如果没有作答，那么used为0 ，如果used为0，那么，下面的值就填写完整；
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResult {
    private Integer used;
    List<QuestionDetail> questionInfo;

}
