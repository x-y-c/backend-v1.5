package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionRepoTest {

    @Autowired
    QuestionRepo questionRepo;
    @Test
    public void test(){
//     System.out.println(   questionRepo.findByQuestionNameLike("%运算符%").size());
    }



}