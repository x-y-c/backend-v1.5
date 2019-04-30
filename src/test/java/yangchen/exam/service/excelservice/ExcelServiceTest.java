package yangchen.exam.service.excelservice;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author YC
 * @date 2019/4/30 14:36
 * O(∩_∩)O)
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ExcelServiceTest {


    @Autowired
    private ExcelService excelService;

    @Test
    public void testExcel2003NoModel() throws IOException {
//        InputStream inputStream = new FileInputStream("d://test1.xlsx");
//        try {
//            // 解析每行结果在listener中处理
//            ExcelListener listener = new ExcelListener();
//
//            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, listener);
//            excelReader.read();
//        } catch (Exception e) {
//
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


//        excelService.excelReader("d://test1.xlsx");
    }
}


//        excelService.excelReader("d://test.xlsx");
//    }
//}