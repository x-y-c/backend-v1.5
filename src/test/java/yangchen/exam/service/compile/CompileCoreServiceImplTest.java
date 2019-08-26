package yangchen.exam.service.compile;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.SourceCode;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.repo.TestCaseRepo;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CompileCoreServiceImplTest {

    @Autowired
    private CompileCoreService compileCoreService;
    @Autowired
    private QuestionRepo questionsRepo;

    @Autowired
    private TestCaseRepo testCaseRepo;
    Gson gson = new Gson();

    @Test
    public void writeSourceCode() throws IOException {
        QuestionNew id = questionsRepo.findById(425).get();
        SourceCode sourceCode = gson.fromJson(id.getSourceCode(), SourceCode.class);
        String code = sourceCode.getKey().get(0).getCode();
        String s = compileCoreService.writeSourceCode(code);
        System.out.println(s);

    }

    @Test
    public void compileCode() throws IOException {
        System.out.println(compileCoreService.compileCode());
    }

    @Test
    public void test333() throws IOException, InterruptedException {
        System.out.println(compileCoreService.getOutput(""));
    }

    /**
     * Java换行符在linux地下是"\n"就可以了，但是在windows下不行，会报错，原因是windows下面他认为"\r\n"才是换行符，而"\n"不是换行符。\r是回车符。
     *
     * @throws IOException
     */
    @Test
    public void getOutput() throws IOException, InterruptedException {
        TestCase testCase = testCaseRepo.findById("72a33f140d2247b2891169ebd3e462dd").get();
        String test1 = compileCoreService.getOutput(testCase.getTestCaseInput()).replaceAll("\r\n", "\n");
        String testCaseOutput = testCase.getTestCaseOutput();

        System.out.println(test1);
//        char[] chars = compileCoreService.getOutput(testCase.getTestCaseInput()).toCharArray();
//        char[] chars1 = testCase.getTestCaseOutput().trim().toCharArray();
//        for (int i=0;i<chars.length;i++){
//            System.out.println(i);
//            if (!(chars[i]==chars1[i])){
//                System.out.println("char?"+chars[i]+"$$$$$"+chars[i]);
//                System.out.println(Integer.valueOf(chars[i])+"!!!!"+Integer.valueOf(chars1[i]));
//            }
//        }
//        System.out.println();
//        System.out.println(testCase.getTestCaseOutput().trim().length());
//        System.out.println(compileCoreService.getOutput(testCase.getTestCaseInput()).equals(testCase.getTestCaseOutput().trim()));

    }


    @Test
    public void test11111111() {
        Double sum1 = 10.0;
        Double sum2 = 10.00;
        System.out.println(sum1.equals(sum2));
    }
}