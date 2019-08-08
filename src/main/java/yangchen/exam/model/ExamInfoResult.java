package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangchen.exam.entity.ExamInfo;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamInfoResult {

    private Integer id;

    private Integer studentNumber;

    private String studentName;

    private Timestamp createdAt;

    private String teacherName;

    private Integer examGroupId;

    private Integer examinationId;

    private Integer examinationScore;

    private String category;

    private Timestamp examStart;

    private Integer ttl;

    private Timestamp examEnd;

    public ExamInfoResult(ExamInfo examInfo) {
        this.id = examInfo.getId();
        this.studentName = examInfo.getStudentName();
        this.category = examInfo.getCategory();
        this.createdAt = examInfo.getCreatedAt();
        this.examinationScore = examInfo.getExaminationScore();
        this.examGroupId = examInfo.getExamGroupId();
        this.examinationId = examInfo.getExaminationId();
        this.examinationScore = examInfo.getExaminationScore();
        this.teacherName = examInfo.getTeacherName();

    }
}
