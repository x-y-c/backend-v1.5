package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.TeacherNew;

public interface TeacherNewRepo extends JpaRepository<TeacherNew,Integer> {
}
