package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelScoreModel {
    private Integer id;
    private Integer studentID;
    private String grade;
    private String name;
    private Double score;
}
