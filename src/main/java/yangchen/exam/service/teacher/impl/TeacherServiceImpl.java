package yangchen.exam.service.teacher.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Teacher;
import yangchen.exam.repo.TeacherRepo;
import yangchen.exam.service.teacher.TeacherService;


/**
 * @author yc
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepo teacherRepo;

    @Override
    public Teacher findTeacherByName(String name) {
        Teacher teacher = teacherRepo.findByTeacherName(name);
        if (teacher != null) {
            return teacher;
        }
        return null;
    }
}
