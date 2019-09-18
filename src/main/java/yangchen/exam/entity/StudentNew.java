package yangchen.exam.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "student_new")
public class StudentNew {

    //主键自增id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //学号
    @Column(name = "student_id")
    private Integer studentId;

    //姓名
    @Column(name = "student_name")
    private String studentName;

    //班级
    @Column(name = "student_grade")
    private String studentGrade;

    //密码
    @Column(name = "password")
    private String password;

    @Column(name = "teacher_id")
    private Integer teacherId;

}
