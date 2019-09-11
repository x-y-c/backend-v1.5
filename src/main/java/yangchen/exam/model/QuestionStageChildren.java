package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangchen.exam.Enum.StageEnum;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionStageChildren {
    private String label; //阶段1，阶段2
    private List<QuestionLastChildren> children; //list
}
