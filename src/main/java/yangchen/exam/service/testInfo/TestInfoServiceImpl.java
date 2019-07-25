package yangchen.exam.service.testInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.TestCase;
import yangchen.exam.service.testInfo.TestCaseService;
import yangchen.exam.service.testInfo.TestInfoService;

import java.util.List;

/**
 * @author YC
 * @date 2019/4/8 17:36
 * O(∩_∩)O)
 */
@Service
public class TestInfoServiceImpl implements TestInfoService {
    @Autowired
    private TestCaseService testCaseService;

    @Override
    public List<TestCase> getTestCaseByQuestionId(String id) {
        List<TestCase> testCaseByQuestionId = testCaseService.findByQuestionId(id);
        return testCaseByQuestionId;
    }
}
