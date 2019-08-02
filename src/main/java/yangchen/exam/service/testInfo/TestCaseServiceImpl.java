package yangchen.exam.service.testInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.TestCase;
import yangchen.exam.repo.TestCaseRepo;

import java.util.List;

/**
 * @author yc
 */
@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Autowired
    private TestCaseRepo TestCaseRepo;

    @Override
    public TestCase addTestCase(TestCase testCase) {
        return TestCaseRepo.save(testCase);
    }

    @Override
    public TestCase updateTestCase(TestCase testCase) {
        return TestCaseRepo.save(testCase);
    }

    @Override
    public TestCase updateTestCaseById(Integer testCaseId) {
        return null;
    }

//

    @Override
    public List<TestCase> findByQuestionId(String questionId) {
        return TestCaseRepo.findByQuestionId(questionId);
    }
}

//    public List<TestCase> findByQid(Integer qid) {
//        return testCaseRepo.findByQid(qid);
//    }