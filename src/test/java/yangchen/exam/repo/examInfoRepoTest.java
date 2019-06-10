package yangchen.exam.repo;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.model.TwoTuple;

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
    public static Logger logger = LoggerFactory.getLogger(examInfoRepoTest.class);

    @Test
    public void test() {

        TwoTuple<String, String> result = new TwoTuple<>("阶段一", "222222");
        Gson gson = new Gson();
        logger.info(gson.toJson(result));

    }

}