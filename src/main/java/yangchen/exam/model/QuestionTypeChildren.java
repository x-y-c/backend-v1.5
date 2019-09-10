package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangchen.exam.Enum.QuestionTypeEnum;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionTypeChildren {
    private String label; //选择题,填空题
    private List<QuestionStageChildren> children;//list
}
