package yangchen.exam.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.repo.QuestionRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class HtmlUtilTest {


    @Autowired
    private QuestionRepo questionRepo;

    @Test
    public void test() {
        QuestionNew hao034 = questionRepo.findByQuestionBh("02359c590cf243d49db06f4b6149a608");
        System.out.println(hao034.getQuestionDescription());
    }

}