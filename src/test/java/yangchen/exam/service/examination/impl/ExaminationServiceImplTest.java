package yangchen.exam.service.examination.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.service.examination.ExaminationService;

/**
 * @author YC
 * @date 2019/5/9 11:19
 * O(∩_∩)O)
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ExaminationServiceImplTest {

    @Autowired
    ExaminationService examinationService;

    @Test
    public void test() {
        examinationService.getQuestionInfo(14);
    }

}