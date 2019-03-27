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
    private Long student_number;


    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "teacher_name")
    private String teacherName;

    @PrePersist
    private void init() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

}
