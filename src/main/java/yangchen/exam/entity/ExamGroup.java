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
@Table(name = "exam_group")
public class ExamGroup {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //描述 如 期末考试练习
    @Column(name = "description")
    private String desc;
    //班级名称
    @Column(name = "class_name")
    private String className;

    @Column(name = "begin_time")
    private Timestamp beginTime;

    //结束时间
    @Column(name = "end_time")
    private Timestamp endTime;
    //考试时间
    @Column(name = "exam_time")
    private Long examTime;

    //发布人
    @Column(name = "exam_teacher")
    private String examTeacher;
}
