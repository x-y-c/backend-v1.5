package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentModifyModel {
    private String teacherName;
    private Integer studentId;
    private String studentName;
    private String studentGrade;
    private Integer type;
    private String password;
}
