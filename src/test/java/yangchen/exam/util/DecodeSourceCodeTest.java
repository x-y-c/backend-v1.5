package yangchen.exam.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.repo.QuestionRepo;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DecodeSourceCodeTest {
    @Autowired
    private QuestionRepo questionRepo;

    @Test
    public void test() {
        Optional<QuestionNew> byId = questionRepo.findById(8);
        QuestionNew questionNew = byId.get();
        System.out.println(DecodeSourceCode.getCode(questionNew.getSourceCode()));
    }


}