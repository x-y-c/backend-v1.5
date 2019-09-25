package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.Teacher;

import java.util.List;

public interface TeacherRepo extends JpaRepository<Teacher, Integer> {
    //通过姓名查询教师信息；
    Teacher findByTeacherName(String name);
    @Query(value = "select distinct id from teacher where active=1 order by id desc",nativeQuery = true)
    List<Integer> getTeacherId();
}
