package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Engineering;

public interface EngineeringRepo extends JpaRepository<Engineering,Integer> {
    Engineering findByStudentId(Integer studentId);
}
