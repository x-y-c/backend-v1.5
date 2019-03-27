package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.TestInfo;

public interface testInfoRepo extends JpaRepository<TestInfo,Integer> {
}
