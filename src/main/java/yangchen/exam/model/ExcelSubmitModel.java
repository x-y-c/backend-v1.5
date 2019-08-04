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

    private String questionBh;
    private String questionName;
    private String Stage;
    private String questionDesc;
    private String src;
    private Double score;
}
