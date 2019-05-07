package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "exam_info")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExamInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_number")
    private Long studentNumber;

    //学生姓名
    @Column(name = "student_name")
    private String studentName;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "teacher_name")
    private String teacherName;


    @Column(name = "exam_group_id")
    private Integer  examGroupId;


    //该试卷成绩
    @Column(name = "examination_score")
    private Integer examinationScore;

    @PrePersist
    private void init() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

}
