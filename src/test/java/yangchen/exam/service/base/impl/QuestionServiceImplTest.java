package yangchen.exam.service.base.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.Question;
import yangchen.exam.model.Category;
import yangchen.exam.service.question.QuestionService;

import java.util.List;

/**
 * @author YC
 * @date 2019/4/16 21:02
 * O(∩_∩)O)
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class QuestionServiceImplTest {
    private static Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImplTest.class);

    @Autowired
    private QuestionService questionService;

//    @Test
//    public void findQuestionByCategory() {
//        List<Question> questionByCategory = questionService.findQuestionByCategory(C);
//        LOGGER.info(questionByCategory.toString());
//    }
}