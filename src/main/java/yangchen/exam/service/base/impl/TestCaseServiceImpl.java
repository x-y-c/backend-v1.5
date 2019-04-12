package yangchen.exam.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.TestCase;
import yangchen.exam.repo.testCaseRepo;
import yangchen.exam.service.base.TestCaseService;

import java.util.List;

/**
 * @author yc
 */
@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Autowired
    private testCaseRepo testCaseRepo;

    @Override
    public TestCase addTestCase(TestCase testCase) {
        return testCaseRepo.save(testCase);
    }

    @Override
    public TestCase updateTestCase(TestCase testCase) {
        return testCaseRepo.save(testCase);
    }

    @Override
    public TestCase updateTestCaseById(Integer testCaseId) {
        return null;
    }

    @Override
    public List<TestCase> findByQid(Integer qid) {
        return testCaseRepo.findByQid(qid);
    }
}
