package yangchen.exam.service.examination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ExamGroupServiceTest {
    @Autowired
    ExamGroupService examGroupService;

    @Test
    public void test(){
        examGroupService.deleteExamInfo(19);
    }




}