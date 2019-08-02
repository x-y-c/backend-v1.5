package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.TestCase;

import java.util.List;

public interface TestCaseRepo extends JpaRepository<TestCase, String > {

    List<TestCase> findByQuestionId(String questionId);

    @Transactional
    @Modifying
    void deleteTestCaseByQuestionId(String questionId);
}
