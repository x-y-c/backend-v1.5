package yangchen.exam.service.question;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionServiceImplTest {

    public static Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImplTest.class);
    @Autowired
    private QuestionService questionService;

    @Test
    public void test() {
        List<String> questionNamesByExamPage = questionService.getQuestionNamesByExamPage(32);
        LOGGER.info(questionNamesByExamPage.toString());
    }
}