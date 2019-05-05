package yangchen.exam.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author YC
 * @date 2019/5/5 17:14
 * O(∩_∩)O)
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RandomUtilTest {

    public static Logger LOGGER = LoggerFactory.getLogger(RandomUtilTest.class);

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            LOGGER.info(String.valueOf(RandomUtil.getRandom(0, 30)));
        }
    }

    @Test
    public void test2() {
        LOGGER.info(String.valueOf(RandomUtil.getRandom(1, 20, 10)));

    }

}