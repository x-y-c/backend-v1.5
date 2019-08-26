package yangchen.exam.service.examination.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.model.ExamPageInfo;
import yangchen.exam.service.examination.ExaminationService;

import java.io.*;
import java.util.List;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ExaminationServiceImplTest {


    @Autowired
    private ExaminationService examinationService;
    public static Logger LOGGER = LoggerFactory.getLogger(ExaminationServiceImplTest.class);

    @Test
    public void test() {
        List<ExamPageInfo> examPageInfo = examinationService.getExamPageInfo(16);
        LOGGER.info(examPageInfo.toString());

    }


    /**
     * InputStream fis=p.getInputStream();
     * //用一个读输出流类去读
     * InputStreamReader isr=new InputStreamReader(fis);
     * BufferedReader br=new BufferedReader(isr);
     * String line=null;
     * while((line=br.readLine())!=null)
     * {System.out.println(line);}
     * }catch (IOException e)
     * {e.printStackTrace();}}
     */

    @Test
    public void test2() throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File("/home/yangchen/sourceCode"));
        builder.command("./main");

        Process start = builder.start();
        InputStream inputStream = start.getInputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(start.getOutputStream()));
        String input = "This is not a program hahahahahaha";
        bufferedWriter.write(input);
        bufferedWriter.close();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        String tmp = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

}