package yangchen.exam.service.student;

import org.springframework.data.domain.Page;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.StudentInfo;
import yangchen.exam.model.StudentModifyModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    void deleteStudentInfo(Integer studentId);


    //添加学生
    JsonResult addStudent(StudentModifyModel student);

    //修改学生
    JsonResult updateStudent(StudentModifyModel student);

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
    Page<StudentNew> getPage(String teacherId,Integer pageNum, Integer pageLimit);

    Page<StudentNew> getGradePage(String teacherId,String grade,Integer pageNum,Integer pageLimit);

    List<String> initGrade();


    JsonResult uploadStudents(String teacherId,List<StudentNew>studentNewsList);
    //JsonResult uploadStudents(List<StudentNew>studentNewsList);

    void downloadStudents(HttpServletResponse response,String grade) throws IOException;

    List<String> getGrades(String teacherId);

}
