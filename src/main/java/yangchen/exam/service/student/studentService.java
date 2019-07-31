package yangchen.exam.service.student;

import org.springframework.data.domain.Page;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.JsonResult;
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
    StudentNew getStudentByStudentId(Integer studentId);

    /**
     * 通过年级查询学生列表
     *
     * @param grade
     * @return
     */
    List<StudentNew> getStudentListByGrade(String grade);

    //修改学生信息
    StudentNew changeStudentInfo(StudentNew student);

    //删除学生信息
    void deleteStudentInfo(StudentNew student);


    //添加学生
    StudentNew addStudent(StudentNew student);

    StudentNew addStudent(StudentInfo studentInfo);

    JsonResult changePassword(Integer studentId, String oldpassword, String password);

    /**
     * 获取全部学生信息
     *
     * @return
     */
    List<StudentNew> getAllStudent();

    /**
     * 分页获取学生信息
     *
     * @param pageNum
     * @param pageLimit
     * @return
     */
    Page<StudentNew> getPage(Integer pageNum, Integer pageLimit);

    List<String> initGrade();


}
