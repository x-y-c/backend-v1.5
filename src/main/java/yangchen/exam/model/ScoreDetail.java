package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YC
 * @date 2019/6/3 15:22
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDetail {
    private Integer examGroupId;
    private String examName;
    private Double score;
}
