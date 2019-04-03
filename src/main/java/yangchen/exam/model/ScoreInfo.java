package yangchen.exam.model;



//examId
//examDesc
// totalScore
//score

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreInfo {

    private Integer  examId;
    private String examDesc;
    private Integer  totalScore;
    private Integer score;

}
