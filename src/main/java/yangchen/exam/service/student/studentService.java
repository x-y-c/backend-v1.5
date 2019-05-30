package yangchen.exam.service.student;

import org.springframework.data.domain.Page;
import yangchen.exam.entity.Student;
import yangchen.exam.model.StudentInfo;

import java.util.List;

/**
 * @author yc
 */
public interface studentService {
    /**
     * 通过学号查询学生
     *
     * @param studentId
     * @return
     */
    Student getStudentByStudentId(Long studentId);

    /**
     * 通过年级查询学生列表
     *
     * @param grade
     * @return
     */
    List<Student> getStudentListByGrade(String grade);

    //修改学生信息
    Student changeStudentInfo(Student student);

    //删除学生信息
    void deleteStudentInfo(Student student);


    //添加学生
    Student addStudent(Student student);

    Student addStudent(StudentInfo studentInfo);

    Student changePassword(Long studentId, String oldpassword,String password);

    /**
     * 获取全部学生信息
     *
     * @return
     */
    List<Student> getAllStudent();

    /**
     * 通过专业获取学生信息
     *
     * @param major
     * @return
     */
    List<Student> getStudentByMajor(String major);

    /**
     * 分页获取学生信息
     *
     * @param pageNum
     * @param pageLimit
     * @return
     */
    Page<Student> getPage(Integer pageNum, Integer pageLimit);


}
