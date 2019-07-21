package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionsRepoTest {

    @Autowired
    private QuestionsRepo questionsRepo;
    @Test
    public void test(){
        System.out.println(questionsRepo.findById("002b95249f984ef48231a4e7dffb42e0"));
    }

}