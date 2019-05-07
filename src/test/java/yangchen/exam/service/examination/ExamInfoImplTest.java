package yangchen.exam.service.examination;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.service.examInfo.ExamInfoService;

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


}