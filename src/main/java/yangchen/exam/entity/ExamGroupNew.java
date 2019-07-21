package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "exam_group_new")
public class ExamGroupNew {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "exam_description")
    private String examDesc;

    @Column(name = "class_name")
    private String className;

    @Column(name = "exam_teacher")
    private String examTeacher;


    @Column(name = "begin_time")
    private Timestamp beginTime;

    @Column(name = "exam_time")
    private Integer examTime;

    @Column(name = "end_time")
    private Timestamp endTime;
}
