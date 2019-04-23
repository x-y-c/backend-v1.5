package yangchen.exam.service.biz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CompileCoreServiceTest {

    @Autowired
    private CompileCoreService compileCoreService;

    private Logger LOGGER = LoggerFactory.getLogger(CompileCoreServiceTest.class);

    @Test
    public void test() throws IOException {
        String code = "#include <stdio.h>\n" +
                "int main(void)\n" +
                "{\n" +
                "printf(\"Hello, world!\");\n" +
                "return 0;" +
                "\n";
        Long time = System.currentTimeMillis();
        String compile = compileCoreService.compile(code, time);
        if (compile==null){
            LOGGER.info("success");
        }else {

        int error = compile.lastIndexOf("error");
        String substring = compile.substring(error);
        LOGGER.info(substring);
        }


    }

}