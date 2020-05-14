package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScoreModel {
    private Integer studentId;
    private String studentGrade;
    private String studentName;
    private Integer score;
    private Boolean finished;
}
