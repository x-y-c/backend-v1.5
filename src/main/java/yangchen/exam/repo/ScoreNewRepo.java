package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ScoreNew;

public interface ScoreNewRepo extends JpaRepository<ScoreNew, Integer> {
}
