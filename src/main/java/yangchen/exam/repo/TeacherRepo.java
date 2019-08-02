package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Teacher;

public interface TeacherRepo extends JpaRepository<Teacher, Integer> {
    //通过姓名查询教师信息；
    Teacher findByTeacherName(String name);
}
