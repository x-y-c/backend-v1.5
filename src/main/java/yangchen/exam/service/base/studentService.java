package yangchen.exam.service.base;

import yangchen.exam.entity.Student;

import java.util.List;

public interface studentService {
    //通过学号查询学生信息
    Student getStudentByStudentId(Long studentId);

    //通过年级查询学生信息;返回多个放在list中；
    List<Student> getStudentListByGrade(String grade);


}
