package yangchen.exam.service.score.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.model.ExcelSubmitModel;
import yangchen.exam.service.score.ScoreService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ScoreServiceImplTest {

    @Autowired
    private ScoreService scoreService;

    @Test
    public void test() throws IOException {
//        HttpServletResponse response;
////        List<ExcelSubmitModel> excelSubmitModels = scoreService.exportSubmit(2);
//        System.out.println(excelSubmitModels.size());

    }

}