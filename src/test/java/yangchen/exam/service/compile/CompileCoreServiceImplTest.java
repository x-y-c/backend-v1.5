package yangchen.exam.service.compile;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.Questions;
import yangchen.exam.model.SourceCode;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.repo.QuestionsRepo;

import java.io.IOException;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CompileCoreServiceImplTest {

    @Autowired
    private CompileCoreService compileCoreService;
    @Autowired
    private QuestionRepo questionsRepo;
    Gson gson = new Gson();

    @Test
    public void writeSourceCode() throws IOException {
        QuestionNew id = questionsRepo.findById(120).get();
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
    public void getOutput() throws IOException {
        System.out.println(compileCoreService.getOutput(""));

    }
}