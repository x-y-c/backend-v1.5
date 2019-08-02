package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.TestInfo;

public interface TestInfoRepo extends JpaRepository<TestInfo,Integer> {


}
