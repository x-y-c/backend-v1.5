package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author YC
 * @date 2019/6/10 16:15
 * O(∩_∩)O)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class studentRepoTest {

    public static Logger Logger = LoggerFactory.getLogger(studentRepoTest.class);
    @Autowired
    private studentRepo studentRepo;

    @Test
    public void getGrade() {

        Logger.info(studentRepo.getGrade().toString());
    }
}