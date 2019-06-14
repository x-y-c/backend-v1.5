package yangchen.exam.service.base.impl;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.Student;
import yangchen.exam.service.student.studentService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StudentServiceImplTest {

    public Logger LOGGER = LoggerFactory.getLogger(StudentServiceImplTest.class);

    @Autowired
    private studentService studentService;

    @Test
    public void getStudentByStudentId() {

        LOGGER.info(studentService.getStudentByStudentId(2015011446L).toString());
    }

    @Test
    public void getStudentListByGrade() {
    }

    @Test
    public void changeStudentInfo() {
        Student studentByStudentId = studentService.getStudentByStudentId(2015011446L);
        studentByStudentId.setPassword("123456");
        Student student = studentService.changeStudentInfo(studentByStudentId);
        LOGGER.info(student.toString());
    }

    @Test
    public void deleteStudentInfo() {
    }

    @Test
    public void addStudent() {
        /*
        *
private Integer id;
private Long studentId;
private String name;
private String password;
private String major;
private String grade;
private Timestamp createdTime;
* private Boolean enabled;
*/
        Student student = new Student();
        student.setEnabled(Boolean.TRUE);
        student.setGrade("软工1503");
        student.setName("杨晨");
        student.setPassword("19961012");
        student.setStudentId(2015011446L);
        studentService.addStudent(student);
    }

    Gson gson = new Gson();

    @Test
    public void getAllStudent() {
        Student studentByStudentId = studentService.getStudentByStudentId(2015011446L);
        LOGGER.info(gson.toJson(studentByStudentId) );
    }
}