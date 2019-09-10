package yangchen.exam.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPractice {
    private String label;//练习题
    private List<QuestionTypeChildren> children;//【】
}
