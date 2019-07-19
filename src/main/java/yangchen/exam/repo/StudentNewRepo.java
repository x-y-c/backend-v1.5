package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.StudentNew;

public interface StudentNewRepo extends JpaRepository<StudentNew,Integer> {
}
