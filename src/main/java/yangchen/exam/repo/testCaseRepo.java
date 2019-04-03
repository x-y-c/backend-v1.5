package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.TestCase;

import java.util.List;

public interface testCaseRepo extends JpaRepository<TestCase, Integer> {
    List<TestCase> findByQid(Integer qid);
}
