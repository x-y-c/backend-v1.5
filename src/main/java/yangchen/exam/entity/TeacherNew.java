package yangchen.exam.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "teacher_new")
public class TeacherNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "teacher_id")
    private Integer teacherId;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "teacher_grade")
    private String teacherGrade;

    @Column(name = "teacher_password")
    private String teacherPassword;
}
