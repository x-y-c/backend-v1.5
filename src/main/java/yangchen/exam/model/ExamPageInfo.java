package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamPageInfo {

    private String studentName;
    private String studentGrade;
    private List<String> questionList;
}