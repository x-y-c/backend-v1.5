package yangchen.exam.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Teacher;
import yangchen.exam.repo.teacherRepo;
import yangchen.exam.service.base.TeacherService;


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
