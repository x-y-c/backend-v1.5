package yangchen.exam.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Student;
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
