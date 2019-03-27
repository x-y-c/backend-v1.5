package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Student;

public interface studentRepo extends JpaRepository<Student,Integer> {
}
