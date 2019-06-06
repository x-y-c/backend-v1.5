package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;

/**
 * @author YC
 * @date 2019/6/6 23:52
 * O(∩_∩)O)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class examInfoRepoTest {
    @Autowired
    private examInfoRepo examInfoRepo;
    public static Logger Logger = LoggerFactory.getLogger(examInfoRepoTest.class);

    @Test
    public void test() {

    }

}