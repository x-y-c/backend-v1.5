package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;

/**
 * @author YC
 * @date 2019/5/12 21:28
 * O(∩_∩)O)
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class examGroupRepoTest {
    @Autowired
    private examGroupRepo examGroupRepo;

    @Test
    public void test() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        examGroupRepo.findByEndTimeAfter(timestamp);
    }

}