package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamInfoNew;

public interface ExamInfoNewRepo extends JpaRepository<ExamInfoNew, Integer> {
}