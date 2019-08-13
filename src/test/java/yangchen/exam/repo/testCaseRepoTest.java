package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.TestCase;

import java.util.UUID;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class testCaseRepoTest {


    @Autowired
    private TestCaseRepo testCaseRepo;

    public static Logger LOGGER = LoggerFactory.getLogger(testCaseRepoTest.class);

    @Test
    public void test() {
        LOGGER.info(String.valueOf(testCaseRepo.findByQuestionId("4797279cd5b3408790814cfe6903d65d")));

    }

    @Test
    public void test2() {
        TestCase testCase = new TestCase();
        testCase.setScoreWeight(20.0);

        testCase.setQuestionId("1111111");
        testCaseRepo.save(testCase);
    }

}