package yangchen.exam.service.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Teacher;
import yangchen.exam.repo.teacherRepo;


/**
 * @author yc
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private teacherRepo teacherRepo;

    @Override
    public Teacher findTeacherByName(String name) {
        Teacher teacher = teacherRepo.findByTeacherName(name);
        if (teacher != null) {
            return teacher;
        }
        return null;
    }
}
