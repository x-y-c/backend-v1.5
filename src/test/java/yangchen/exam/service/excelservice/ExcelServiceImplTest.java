package yangchen.exam.service.excelservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author YC
 * @date 2019/5/5 9:59
 * O(∩_∩)O)
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExcelServiceImplTest {

    @Autowired
    private ExcelServiceImpl excelService;


    @Test
    public void test(){
        excelService.readerExcelForQuestion("11111");
    }

}