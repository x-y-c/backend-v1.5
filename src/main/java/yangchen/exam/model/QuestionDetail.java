package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YC
 * @date 2019/5/9 10:59
 * O(∩_∩)O)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDetail {
    private String title;
    private String question;
    private String id;
    private String customBh;
    private String isProgramBlank;
    private String src;
    private Integer score;
    private String codeHistory;
}
