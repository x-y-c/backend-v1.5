package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamGroupNew;

public interface ExamGroupNewRepo extends JpaRepository<ExamGroupNew, Integer> {
}
