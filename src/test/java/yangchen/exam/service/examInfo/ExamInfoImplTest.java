package yangchen.exam.service.examInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.Examination;

/**
 * @author YC
 * @date 2019/5/6 11:24
 * O(∩_∩)O)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExamInfoImplTest {

    @Autowired
    private ExamInfoService examInfo;
    private static Logger LOGGER = LoggerFactory.getLogger(ExamInfoImplTest.class);

    @Test
    public void test() {
        Examination examination = examInfo.createExamInfo("阶段一");
        LOGGER.info(examination.toString());
    }

    @Test
    public void test2() {
        examInfo.createExamInfo("阶段一", 4);
    }

}