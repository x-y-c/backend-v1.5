package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.TestCase;

public interface testCaseRepo extends JpaRepository<TestCase,Integer> {
}
