package yangchen.exam.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExcelSubmitModel {

    private Integer studentNumber;
    private String studentName;
    private Integer examPaperId;
    private String questionBh;
    private String questionName;
    private String stage;
    private String questionDesc;
    private String src;
    private Double score;
}
