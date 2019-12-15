package yangchen.exam.service.testInfo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.repo.TestCaseRepo;
import yangchen.exam.service.testInfo.TestCaseService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author yc
 */
@Service
public class TestCaseServiceImpl implements TestCaseService {

    public static Logger LOGGER = LoggerFactory.getLogger(TestCaseServiceImpl.class);
    private List<TestCase> modifyList = new ArrayList<>();
    private List<String> deletedList = new ArrayList<>();

    @Autowired
    private TestCaseRepo testCaseRepo;

    @Override
    public void resetList() {
        modifyList.clear();
        deletedList.clear();
    }


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

    private void initModifyList(String questionBh) {
        if (modifyList.size() == 0) {
            List<TestCase> testCaseList = testCaseRepo.findByQuestionId(questionBh);
            modifyList.addAll(testCaseList);
        }
    }
//

    @Override
    public List<TestCase> findByQuestionId(String questionId) {
        return testCaseRepo.findByQuestionId(questionId);
    }

    @Override
    public JsonResult modifyTestCase(String testCaseBh, Double scoreWeight, String testCaseInput,
                                     String testCaseOutput, String questionId, Integer operate) {

        initModifyList(questionId);
//        LOGGER.info("the init list size=[{}]", modifyList.size());
//        LOGGER.info("the init list =[{}]", modifyList.toString());
        Double sum = 0.0;//计算权重
        switch (operate) {
            // 增加
            case 1:
                TestCase testCase = new TestCase(testCaseBh, scoreWeight, testCaseInput, testCaseOutput, questionId);
                modifyList.add(testCase);
//                LOGGER.info("the add list size=[{}]", modifyList.size());
//                LOGGER.info("the add list =[{}]", modifyList.toString());
                LOGGER.info("增加的测试用例：[{}]",testCase.toString());
                break;
            //修改
            case 2:
                Integer testCaseIndex = getTestCase(modifyList, testCaseBh);
                if (testCaseIndex == -1) {

                    break;
                }
                TestCase modifyTestCase = modifyList.get(testCaseIndex);
                TestCase modifyedTestCode = updateTestCase(modifyTestCase, scoreWeight, testCaseInput, testCaseOutput);
                modifyList.set(testCaseIndex, modifyedTestCode);
//                LOGGER.info("the modify list size=[{}]", modifyList.size());
//                LOGGER.info("the modify list =[{}]", modifyList.toString());
                LOGGER.info("修改的测试用例[{}]，testCaseIndex=[{}]",modifyedTestCode.toString(),testCaseIndex);
                break;
            //delete
            case 3:
                deletedTestCase(deletedList, modifyList, testCaseBh);
                LOGGER.info("the delete list size=[{}]", modifyList.size());
                LOGGER.info("the delete list =[{}]", modifyList.toString());
                LOGGER.info("deleteList is =[{}]", deletedList.toString());
                break;
            case 4:
                for (TestCase testCase1 : modifyList) {
                    sum = sum + testCase1.getScoreWeight();
                }
                if (sum.equals(100.00)) {
                    modifyList.forEach(testCase1 -> {
                        testCaseRepo.save(testCase1);
                    });
                    deletedList.forEach(s -> {
                        testCaseRepo.deleteTestCaseByTestCaseBh(s);
                    });
                    return JsonResult.succResult(null);
                } else {
                    return JsonResult.errorResult(ResultCode.TESTCASE_SCORE_WEIGHT_ERROR, "权重不是100%", sum);
                }

        }
        return JsonResult.succResult(null);

    }

    private TestCase updateTestCase(TestCase testCase, Double scoreWeight, String testCaseInput, String testCaseOutput
    ) {
        testCase.setScoreWeight(scoreWeight);
        testCase.setTestCaseInput(testCaseInput);
        testCase.setTestCaseOutput(testCaseOutput);
        return testCase;
    }

    private Integer getTestCase(List<TestCase> list, String testCaseBh) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTestCaseBh().equals(testCaseBh)) {
                return i;
            }
        }
        return -1;
    }

    private void deletedTestCase(List<String> list, List<TestCase> testList, String testCaseBh) {
//        for (TestCase testCase : testList) {
//            if (testCase.getTestCaseBh().equals(testCaseBh)) {
//                testList.remove(testCase);
//            }
//        }
        Iterator<TestCase> it = testList.iterator();
        while (it.hasNext()) {
            TestCase next = it.next();
            if (next.getTestCaseBh().equals(testCaseBh)) {
                it.remove();
            }

        }
        list.add(testCaseBh);
    }
}
