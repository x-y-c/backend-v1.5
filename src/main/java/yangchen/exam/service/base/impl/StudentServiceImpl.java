package yangchen.exam.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Student;
import yangchen.exam.model.StudentInfo;
import yangchen.exam.repo.studentRepo;
import yangchen.exam.service.base.studentService;

import java.util.List;

/**
 * @author yc
 */
@Service
public class StudentServiceImpl implements studentService {
    @Autowired
    private studentRepo studentRepo;


    @Override
    public Student getStudentByStudentId(Long studentId) {
        return studentRepo.findByStudentId(studentId);
    }

    @Override
    public List<Student> getStudentListByGrade(String grade) {
        return studentRepo.findByGrade(grade);
    }

    @Override
    public Student changeStudentInfo(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public void deleteStudentInfo(Student student) {
        studentRepo.delete(student);
    }

    @Override
    public Student addStudent(Student student) {
        if (student.getPassword() == null || student.getPassword().length() <= 0) {
            student.setPassword("123456");
        }
        if (student.getEnabled() == null) {
            student.setEnabled(true);
        }
        return studentRepo.save(student);
    }

    @Override
    public Student addStudent(StudentInfo studentInfo) {
        Student student = new Student();
        student.setName(studentInfo.getName());
        student.setEnabled(true);
        student.setStudentId(studentInfo.getStudentId());
        student.setPassword("123456");
        student.setMajor(studentInfo.getMajor());
        student.setGrade(studentInfo.getGrade());

        return studentRepo.save(student);
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepo.findAll();
    }

    @Override
    public List<Student> getStudentByMajor(String major) {
        return studentRepo.findByMajor(major);
    }
}
