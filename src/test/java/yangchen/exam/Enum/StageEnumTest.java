package yangchen.exam.Enum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StageEnumTest {


    @Test
    public void test(){
     System.out.println(StageEnum.getStageCode("阶段一"));
     System.out.println(StageEnum.getStageName("1000301"));
    }

    @Test
    public void test2(){
        for (QuestionTypeEnum q: QuestionTypeEnum.values()){
            System.out.println(q.getQuestionTypeName());
        }
    }

}