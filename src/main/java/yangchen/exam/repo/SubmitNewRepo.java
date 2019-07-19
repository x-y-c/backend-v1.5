package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.SubmitNew;

public interface SubmitNewRepo extends JpaRepository<SubmitNew, Integer> {
}
