package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "excelEngineering")
public class Engineering {

    //主键自增id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //学号
    @Column(name = "student_id")
    private Integer studentId;

    //学期
    @Column(name = "student_year")
    private Integer studentYear;

    //班级
    @Column(name = "student_grade")
    private String studentGrade;

    //教师
    @Column(name = "teacher_id")
    private Integer teacherId;

    @Column(name = "score")
    private Integer score;

}

