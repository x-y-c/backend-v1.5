package yangchen.exam.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionSelectModel {

    Integer id;
    String questionBh;
    String answer;
    String questionName;
    String questionDetails;
    String stage;
    String difficulty;
}
