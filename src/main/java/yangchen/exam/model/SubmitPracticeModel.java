package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitPracticeModel {

    private Integer studentId;
    private String studentName;
    private Integer questionId;
    private String src;
    private Integer score;
    private Timestamp submitTime;

}
