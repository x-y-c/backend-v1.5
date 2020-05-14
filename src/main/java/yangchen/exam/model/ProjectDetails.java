package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangchen.exam.entity.QuestionNew;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetails {

    private String ProjectName;
    private String className;
    private Timestamp beginTime;
    private Integer examTime;
    private List<QuestionNew> questionList;

}
