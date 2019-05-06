package yangchen.exam.service.examInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Test
    public void test(){
        examInfo.createExamInfo("阶段一");
    }

    @Test
    public void test2(){
        examInfo.createExamInfo("阶段一",4);
    }

}