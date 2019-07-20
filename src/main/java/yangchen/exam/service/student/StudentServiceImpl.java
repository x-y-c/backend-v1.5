package yangchen.exam.service.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.StudentInfo;
import yangchen.exam.repo.studentRepo;

import java.util.List;

/**
 * @author yc
 */
@Service
public class StudentServiceImpl implements studentService {
    @Autowired
    private studentRepo studentRepo;

@Cacheable(value = "student")
    @Override
    public StudentNew getStudentByStudentId(Integer studentId) {
        return studentRepo.findByStudentId(studentId);
    }

    @Override
    public List<StudentNew> getStudentListByGrade(String grade) {
        return studentRepo.findByStudentGrade(grade);
    }

    @Override
    public StudentNew changeStudentInfo(StudentNew student) {
        return studentRepo.save(student);
    }

    public StudentNew changePassword(Integer studentId, String oldpasswprd, String password) {
        StudentNew byStudentId = studentRepo.findByStudentId(studentId);
        if (byStudentId != null) {
            if (byStudentId.getPassword().equals(oldpasswprd)) {
                byStudentId.setPassword(password);
                StudentNew save = studentRepo.save(byStudentId);
                return save;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    public void deleteStudentInfo(StudentNew student) {
        studentRepo.delete(student);
    }

    @Override
    public StudentNew addStudent(StudentNew student) {
        if (student.getPassword() == null || student.getPassword().length() <= 0) {
            student.setPassword("123456");
        }

        StudentNew byStudentId = studentRepo.findByStudentId(student.getStudentId());
        if (byStudentId != null) {
            byStudentId.setStudentGrade((student.getStudentGrade()));
            return studentRepo.save(byStudentId);
        }
        return studentRepo.save(student);
    }


    /**
     * 用在excel处理的部分，excel读取的是 studentInfo
     *
     * @param studentInfo
     * @return
     */
    @Override
    public StudentNew addStudent(StudentInfo studentInfo) {
        StudentNew student = new StudentNew();
        student.setStudentName(studentInfo.getName());
        student.setStudentId(studentInfo.getStudentId());
        student.setPassword("123456");

        student.setStudentGrade(studentInfo.getGrade());

        return studentRepo.save(student);
    }

    @Override
    public List<StudentNew> getAllStudent() {
        return studentRepo.findAll();
    }


    @Override
    public Page<StudentNew> getPage(Integer pageNum, Integer pageLimit) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit);
        return studentRepo.findAll(pageable);
    }

    @Override
    public List<String> initGrade() {
        return studentRepo.getGrade();
    }
}
