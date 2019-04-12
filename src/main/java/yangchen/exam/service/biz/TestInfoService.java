package yangchen.exam.service.biz;

import yangchen.exam.entity.TestCase;

import java.util.List;

/**
 * @author YC
 */
public interface TestInfoService {

    /**
     *
     * @param id question的id
     * @return 返回该题目的测试用例信息；
     */
     List<TestCase> getTestCaseByQuestionId(Integer id);

}
