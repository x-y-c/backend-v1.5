package yangchen.exam.service.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.TestCase;
import yangchen.exam.service.base.QuestionService;
import yangchen.exam.service.base.TestCaseService;
import yangchen.exam.service.biz.TestInfoService;

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
    public List<TestCase> getTestCaseByQuestionId(Integer id) {
        List<TestCase> testCaseByQuestionId = testCaseService.findByQid(id);
        return testCaseByQuestionId;
    }
}
