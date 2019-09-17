package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.util.UrlImageUrl;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class examGroupRepoTest {

    @Autowired
    private ExamGroupRepo examGroupRepo;

    public static Logger LOGGER = LoggerFactory.getLogger(examGroupRepoTest.class);

    @Test
    public void test() {
        LOGGER.info(examGroupRepo.findByExamTeacher("yangchen").toString());
//        UrlImageUrl.test();
    }
}