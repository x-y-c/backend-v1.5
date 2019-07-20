package yangchen.exam.service.examination.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.model.ExamPageInfo;
import yangchen.exam.service.examination.ExaminationService;

import java.util.List;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ExaminationServiceImplTest {


    @Autowired
    private ExaminationService examinationService;
    public static Logger LOGGER = LoggerFactory.getLogger(ExaminationServiceImplTest.class);

    @Test
    public void test() {
        List<ExamPageInfo> examPageInfo = examinationService.getExamPageInfo(16);
        LOGGER.info(examPageInfo.toString());

    }

}