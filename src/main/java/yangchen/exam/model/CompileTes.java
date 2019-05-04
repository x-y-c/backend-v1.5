package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/4 11:11
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompileTes {
    private List input;
    private List output;
    private Long timeLimit;
    private Long memoryLimit;
    private Integer judgeId;
    private String src;
}
