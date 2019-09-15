package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionLogModel {

    private String name;
    private String optionDo;
    private String questionId;
    private Timestamp editTime;
    private String questionName;

}
