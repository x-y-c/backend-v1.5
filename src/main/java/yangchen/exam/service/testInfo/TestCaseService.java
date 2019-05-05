package yangchen.exam.service.testInfo;

import yangchen.exam.entity.TestCase;

import java.util.List;

/**
 * @author YC
 */
public interface TestCaseService {

    //添加测试用例
    TestCase addTestCase(TestCase testCase);

    //更新测试用例
    TestCase updateTestCase(TestCase testCase);

    //通过 id更新测试用例
    TestCase updateTestCaseById(Integer testCaseId);

    //通过题目查找测试用例
    List<TestCase> findByQid(Integer qid);
}
