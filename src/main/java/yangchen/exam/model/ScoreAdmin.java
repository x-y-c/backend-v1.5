package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YC
 * @date 2019/6/3 19:14
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreAdmin {
    private Integer studentId;
    private Integer score;
    private String studentName;
    private String examDesc;
}
