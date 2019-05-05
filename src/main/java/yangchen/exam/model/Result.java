package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YC
 * @date 2019/5/4 22:13
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String result;
    private String timeused;
    private String memoryused;
    private String errormessage;
}
